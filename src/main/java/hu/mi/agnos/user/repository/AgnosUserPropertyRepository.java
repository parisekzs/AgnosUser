/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.agnos.user.entity.AgnosUser;
import hu.mi.agnos.user.entity.AgnosUserRoles;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import org.springframework.util.Assert;

/**
 *
 * @author parisek
 */
public class AgnosUserPropertyRepository extends PropertyRepository<AgnosUser, String> {

    private String path;

    @Override
    public void setURI(String path) {
        this.path = path;
        this.uri = new StringBuilder(path)
                .append(path.endsWith("/") ? "" : "/")
                .append("application-users.properties")
                .toString();
    }

    @Override
    public List<AgnosUser> findAll() {
        List<AgnosUser> result = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            for (Object userName : prop.keySet()) {

                String valueString = prop.getProperty((String) userName);
                if (valueString != null) {
                    AgnosUser user = parseEntityFromJSONString((String) userName, valueString);
                    AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository();
                    userRolesRepository.setURI(path);
                    Optional<AgnosUserRoles> userRoles = userRolesRepository.findById((String) userName);
                    if (userRoles.isPresent()) {
                        user.setRoles(userRoles.get().getRoles());
                    }
                    result.add(user);
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    @Override
    public Optional<AgnosUser> findById(String userName) {
        Assert.notNull(userName, ID_MUST_NOT_BE_NULL);

        try (FileInputStream input = new FileInputStream(this.uri)) {
            AgnosUser result = null;
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            String valueString = prop.getProperty(userName);
            if (valueString != null) {
                result = parseEntityFromJSONString(userName, valueString);
                AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository();
                userRolesRepository.setURI(path);
                Optional<AgnosUserRoles> userRoles = userRolesRepository.findById(userName);
                if (userRoles.isPresent()) {
                    result.setRoles(userRoles.get().getRoles());
                }
            }
            return Optional.ofNullable(result);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<AgnosUser> save(AgnosUser user) {
        Assert.notNull(user, "Entity must not be null.");
        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String value = mapper.writeValueAsString(user);

            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(user.getName(), value);

            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            logger.error(io.getMessage());
            return Optional.empty();
        }
        AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository();
        userRolesRepository.setURI(path);
        return userRolesRepository
                .save(new AgnosUserRoles(user))
                .isPresent()
                        ? Optional.ofNullable(user)
                        : Optional.empty();

    }

    @Override
    public Optional<AgnosUser> deleteById(String userName) {
        Optional<AgnosUser> deleted = findById(userName);

        if (deleted.isPresent()) {
            try (OutputStream output = new FileOutputStream(uri)) {

                Properties prop = new Properties();
                // set the properties value
                prop.remove(userName);

                // save properties to project root folder
                prop.store(output, null);
                return deleted;
            } catch (IOException io) {
                logger.error(io.getMessage());
                return Optional.empty();
            }
        }
        AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository();
        userRolesRepository.setURI(path);
        return userRolesRepository
                .deleteById(userName)
                .isPresent()
                        ? deleted
                        : Optional.empty();
    }


}
