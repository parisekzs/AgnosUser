/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.UserRoles;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class RoleRepo extends AbstractPropertyRepo<Role, String> {

    public RoleRepo(String path) {
        super(Role.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
        this.uri = new StringBuilder(this.path)
                .append("auth/application-roles.properties")
                .toString();
        this.tmpUri = new StringBuilder(this.path)
                .append("auth/tmp-application-roles.properties")
                .toString();

    }

    @Override
    public List<Role> findAll() {
        List<Role> result = new ArrayList<>();
        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            for (Object roleName : prop.keySet()) {

                String valueString = prop.getProperty((String) roleName);
                if (valueString != null) {
                    Role role = parseEntityFromJSONString((String) roleName, valueString);
                    UserRolesRepo userRolesRepository = new UserRolesRepo(this.path);
                    for (UserRoles ur : userRolesRepository.findAllByRoleName((String) roleName)) {
                        role.addUser(ur.getName());
                    }
                    result.add(role);
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }    
    
    @Override
    public Role save(Role role) {
        Assert.notNull(role, "Entity must not be null.");
        List<Role> storedRoles = findAll();
        if (storedRoles.contains(role)) {
            storedRoles.remove(role);
        }
        storedRoles.add(role);
        storeToFile(storedRoles);        
        UserRolesRepo userRolesRepository = new UserRolesRepo(path);
        UserRoles userRoles = userRolesRepository.save(role);
        return role;
        //return userRoles != null ? role : null;
    }   
    
    
    @Override
    public void deleteById(String roleName) {
        Optional<Role> optRole = findById(roleName);
        if (optRole.isPresent()) {
            Role role = optRole.get();
            List<Role> storedRole = findAll();
            storedRole.remove(role);
            storeToFile(storedRole);

            UserRolesRepo userRolesRepository = new UserRolesRepo(this.path);
            for (UserRoles userRoles : userRolesRepository.findAllByRoleName(roleName)) {
                userRoles.getRoles().remove(roleName);
                userRolesRepository.save(userRoles);
            }
        }
    }

}
