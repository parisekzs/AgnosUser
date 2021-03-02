/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.entity.AgnosUserRoles;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author parisek
 */
public class AgnosUserRolesPropertyRepository extends PropertyRepository<AgnosUserRoles, String> {

    @Override
    public void setURI(String path) {
        this.uri = new StringBuilder(path)
                .append(path.endsWith("/") ? "" : "/")
                .append("application-users-roles.properties")
                .toString();
    }

    public List<AgnosUserRoles> findAllByRoleName(String roleName) {
        List<AgnosUserRoles> result = new ArrayList<>();
        
        for(AgnosUserRoles userRoles : findAll()){
            if(userRoles.getRoles().contains(roleName)){
                result.add(userRoles);
            }
        } 
        return result;
    }
}
