/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.entity.AgnosRole;
import hu.mi.agnos.user.entity.AgnosUserRoles;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

/**
 *
 * @author parisek
 */
public class AgnosRolePropertyRepository extends PropertyRepository<AgnosRole, String> {

    private String path;

    @Override
    public void setURI(String path) {
        this.path = path;
        this.uri = new StringBuilder(path)
                .append(path.endsWith("/") ? "" : "/")
                .append("application-roles.properties")
                .toString();
    }

    @Override
    public Optional<AgnosRole> deleteById(String roleName) {
        Optional<AgnosRole> deleted = findById(roleName);

        if (deleted.isPresent()) {
            try (OutputStream output = new FileOutputStream(uri)) {

                Properties prop = new Properties();
                // set the properties value
                prop.remove((String) roleName);

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                logger.error(io.getMessage());
                return Optional.empty();
            }
            AgnosUserRolesPropertyRepository userRolesRepository = new AgnosUserRolesPropertyRepository();
            userRolesRepository.setURI(path);
            for(AgnosUserRoles userRoles : userRolesRepository.findAllByRoleName(roleName)){
                userRoles.getRoles().remove(roleName);
                userRolesRepository.save(userRoles);
            }            
        }
        return deleted;
    }

}
