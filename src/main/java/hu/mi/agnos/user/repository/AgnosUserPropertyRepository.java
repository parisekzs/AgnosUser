/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.agnos.user.entity.dao.AgnosDAOUser;
import hu.mi.agnos.user.entity.dao.AgnosDAOUserRoles;
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
import javax.annotation.PostConstruct;
import org.springframework.util.Assert;

/**
 *
 * @author parisek
 */
public class AgnosUserPropertyRepository extends PropertyRepository<AgnosDAOUser, String> {

    public AgnosUserPropertyRepository(String path) {
        super(AgnosDAOUser.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
        
        this.uri = new StringBuilder(this.path)
                .append("application-users.properties")
                .toString();
        super.uri = new StringBuilder(this.path)
                .append("application-users.properties")
                .toString();
    }

    @Override
    public List<AgnosDAOUser> findAll() {
        List<AgnosDAOUser> result = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            for (Object userName : prop.keySet()) {

                String valueString = prop.getProperty((String) userName);
                if (valueString != null) {
                    AgnosDAOUser user = parseEntityFromJSONString((String) userName, valueString);
                    AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(this.path);
                    Optional<AgnosDAOUserRoles> userRoles = userRolesRepository.findById((String) userName);
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
    public Optional<AgnosDAOUser> findById(String userName) {
        Assert.notNull(userName, ID_MUST_NOT_BE_NULL);

        try (FileInputStream input = new FileInputStream(this.uri)) {
            AgnosDAOUser result = null;
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            String valueString = prop.getProperty(userName);
            if (valueString != null) {
                result = parseEntityFromJSONString(userName, valueString);
                AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(path);
                Optional<AgnosDAOUserRoles> userRoles = userRolesRepository.findById(userName);
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
    public Optional<AgnosDAOUser> save(AgnosDAOUser user) {        
        Assert.notNull(user, "Entity must not be null.");
        List<AgnosDAOUser> oldUsers = findAll();
        
        user.setEncodedPassword(user.getPlainPassword());
        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            
            Properties prop = new Properties();
            for(AgnosDAOUser oldUser : oldUsers){
                if(user.getName().equals(oldUser.getName()) ){
                    String value = mapper.writeValueAsString(user);
                    prop.setProperty(user.getName(), value);
                }
                else{
                    String value = mapper.writeValueAsString(oldUser);
                    prop.setProperty(oldUser.getName(), value);
                }
            }
            // set the properties value
            

            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            logger.error(io.getMessage());
            return Optional.empty();
        }
        AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(path);
        return userRolesRepository
                .save(new AgnosDAOUserRoles(user))
                .isPresent()
                        ? Optional.ofNullable(user)
                        : Optional.empty();

    }

        
    public Optional<AgnosDAOUser> save(String oldUserName, AgnosDAOUser newUser) {        
        Assert.notNull(newUser, "Entity must not be null.");
        Assert.notNull(oldUserName, "Entity must not be null.");
     
        List<AgnosDAOUser> oldUsers = findAll();
        
        newUser.setEncodedPassword(newUser.getPlainPassword());
        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            
            Properties prop = new Properties();
            for(AgnosDAOUser oldUser : oldUsers){
                if(oldUserName.equals(oldUser.getName()) ){
                    String value = mapper.writeValueAsString(newUser);
                    prop.setProperty(newUser.getName(), value);
                }
                else{
                    String value = mapper.writeValueAsString(oldUser);
                    prop.setProperty(oldUser.getName(), value);
                }
            }
            // set the properties value
            

            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException io) {
            logger.error(io.getMessage());
            return Optional.empty();
        }
        AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(path);
        return userRolesRepository
                .save(new AgnosDAOUserRoles(newUser))
                .isPresent()
                        ? Optional.ofNullable(newUser)
                        : Optional.empty();

    }
    
    
    @Override
    public Optional<AgnosDAOUser> deleteById(String userName) {
        Optional<AgnosDAOUser> deleted = findById(userName);

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
        AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(path);
        return userRolesRepository
                .deleteById(userName)
                .isPresent()
                        ? deleted
                        : Optional.empty();
    }

}
