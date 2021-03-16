/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.UserRoles;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import javax.annotation.PostConstruct;

/**
 *
 * @author parisek
 */
public class RoleRepo extends AbstractRepo<Role, String> {

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

                Properties prop = new Properties();
                // set the properties value
                prop.remove((String) roleName);

                // save properties to project root folder
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
