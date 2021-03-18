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
import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.entity.UserRoles;
import java.io.File;
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
public class UserRepo extends AbstractPropertyRepo<User, String> {

    public UserRepo(String path) {
        super(User.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
        this.uri = new StringBuilder(this.path)
                .append("auth/application-users.properties")
                .toString();
        this.tmpUri = new StringBuilder(this.path)
                .append("auth/tmp-application-users.properties")
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
                    UserRolesRepo userRolesRepository = new UserRolesRepo(this.path);
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
                UserRolesRepo userRolesRepository = new UserRolesRepo(path);
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

    @Override
    public User save(User user) {
        Assert.notNull(user, "Entity must not be null!");
        List<User> storedUsers = findAll();
        if (storedUsers.contains(user)) {
            if (user.getEncodedPassword() == null) {
                User storedUser = storedUsers.get(storedUsers.indexOf(user));
                user.setEncodedPassword(storedUser.getEncodedPassword());
            }
            storedUsers.remove(user);
        }
        storedUsers.add(user);
        storeToFile(storedUsers);
        UserRolesRepo userRolesRepository = new UserRolesRepo(path);
        UserRoles userRoles = userRolesRepository.save(new UserRoles(user));
        return userRoles != null ? user : null;
    }

    @Override
    public void deleteById(String userName) {
        Optional<User> optUser = findById(userName);
        if (optUser.isPresent()) {
            User user = optUser.get();
            List<User> storedUsers = findAll();
            storedUsers.remove(user);
            storeToFile(storedUsers);
        }
        new UserRolesRepo(path)
                .deleteById(userName);
    }
}
