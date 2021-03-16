/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.entity.UserRoles;
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
public class UserRepo extends AbstractRepo<User, String> {

    public UserRepo(String path) {
        super(User.class, path);
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
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            for (Object userName : prop.keySet()) {

                String valueString = prop.getProperty((String) userName);
                if (valueString != null) {
                    User user = parseEntityFromJSONString((String) userName, valueString);
                    UserRolesRepository userRolesRepository = new UserRolesRepository(this.path);
                    Optional<UserRoles> userRoles = userRolesRepository.findById((String) userName);
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
    public Optional<User> findById(String userName) {
        Assert.notNull(userName, ID_MUST_NOT_BE_NULL);

        try (FileInputStream input = new FileInputStream(this.uri)) {
            User result = null;
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            String valueString = prop.getProperty(userName);
            if (valueString != null) {
                result = parseEntityFromJSONString(userName, valueString);
                UserRolesRepository userRolesRepository = new UserRolesRepository(path);
                Optional<UserRoles> userRoles = userRolesRepository.findById(userName);
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

    private void storeToProperties(Properties prop, ObjectMapper mapper, User user, User oldUser) throws JsonProcessingException {
        if (user.getName().equals(oldUser.getName())) {
            if (user.getEncodedPassword() == null) {
                user.setEncodedPassword(oldUser.getEncodedPassword());
            }
        }
        storeToProperties(prop, mapper, oldUser);
    }

    private void storeToProperties(Properties prop, ObjectMapper mapper, User user) throws JsonProcessingException {
        String value = mapper.writeValueAsString(user);
        prop.setProperty(user.getName(), value);

    }

    @Override
    public User save(User user) {
        Assert.notNull(user, "Entity must not be null!");
        List<User> oldUsers = findAll();

        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            boolean hasUser = false;
            Properties prop = new Properties();
            //copy all property to new file
            for (User oldUser : oldUsers) {
                if (user.getName().equals(oldUser.getName())) {
                    hasUser = true;
                }
                storeToProperties(prop, mapper, user, oldUser);
            }
            //if there is no such entity yet
            if (!hasUser) {
                storeToProperties(prop, mapper, user);
            }
            
            prop.store(output, null);
        } catch (IOException io) {
            logger.error(io.getMessage());
            return null;
        }
        UserRolesRepository userRolesRepository = new UserRolesRepository(path);
        UserRoles userRoles = userRolesRepository.save(new UserRoles(user));
        return userRoles != null ? user : null;
    }

    public Optional<User> save(String oldUserName, User newUser) {
        Assert.notNull(newUser, "Entity must not be null.");
        Assert.notNull(oldUserName, "Entity must not be null.");

        List<User> oldUsers = findAll();

        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            Properties prop = new Properties();
            for (User oldUser : oldUsers) {
                if (oldUserName.equals(oldUser.getName())) {
                    if (newUser.getEncodedPassword() == null) {
                        newUser.setEncodedPassword(oldUser.getEncodedPassword());
                    }
                    String value = mapper.writeValueAsString(newUser);
                    prop.setProperty(newUser.getName(), value);
                } else {
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
        UserRoles userRoles = new UserRolesRepository(path)
                .save(new UserRoles(newUser));
        return userRoles != null ? Optional.of(newUser) : Optional.empty(); 
    }

    @Override
    public void deleteById(String userName) {
        Optional<User> deleted = findById(userName);

        if (deleted.isPresent()) {
            try (OutputStream output = new FileOutputStream(uri)) {

                Properties prop = new Properties();
                // set the properties value
                prop.remove(userName);

                // save properties to project root folder
                prop.store(output, null);
            } catch (IOException io) {
                logger.error(io.getMessage());
            }
        }
        new UserRolesRepository(path)
                .deleteById(userName);
    }
}
