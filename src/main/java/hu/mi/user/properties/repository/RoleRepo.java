/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.UserRoles;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;

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
                .append("application-roles.properties")
                .toString();
    }

    @Override
    public void deleteById(String roleName) {
        if (findById(roleName).isPresent()) {
            try (OutputStream output = new FileOutputStream(uri)) {
                List<Role> storedRoles = findAll();

                ObjectMapper mapper = new ObjectMapper();
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

                Properties prop = new Properties();
                for (Role storedRole : storedRoles) {
                    if (!storedRole.getName().equals(roleName)) {
                        storeToProperties(prop, mapper, storedRole);
                    }
                }
                prop.store(output, null);
            } catch (IOException io) {
                logger.error(io.getMessage());
            }

            UserRolesRepository userRolesRepository = new UserRolesRepository(this.path);
            for (UserRoles userRoles : userRolesRepository.findAllByRoleName(roleName)) {
                userRoles.getRoles().remove(roleName);
                userRolesRepository.save(userRoles);
            }
        }
    }

}
