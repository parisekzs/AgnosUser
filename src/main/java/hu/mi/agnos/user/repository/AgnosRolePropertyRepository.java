/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.entity.dao.AgnosDAORole;
import hu.mi.agnos.user.entity.dao.AgnosDAOUserRoles;
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
public class AgnosRolePropertyRepository extends PropertyRepository<AgnosDAORole, String> {

    public AgnosRolePropertyRepository(String path) {
        super(AgnosDAORole.class, path);
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
            AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository(this.path);
            for (AgnosDAOUserRoles userRoles : userRolesRepository.findAllByRoleName(roleName)) {
                userRoles.getRoles().remove(roleName);
                userRolesRepository.save(userRoles);
            }
        }
    }

}
