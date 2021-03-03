/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.entity.AgnosDAOUserRoles;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author parisek
 */
public class AgnosUserRolesPropertyRepository extends PropertyRepository<AgnosDAOUserRoles, String> {

    public AgnosUserRolesPropertyRepository(String path) {
        super(AgnosDAOUserRoles.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
//        super.uri 
        this.uri = new StringBuilder(this.path)
                .append("application-users-roles.properties")
                .toString();
    }

    public List<AgnosDAOUserRoles> findAllByRoleName(String roleName) {
        List<AgnosDAOUserRoles> result = new ArrayList<>();
        
        for(AgnosDAOUserRoles userRoles : findAll()){
            if(userRoles.getRoles().contains(roleName)){
                result.add(userRoles);
            }
        } 
        return result;
    }
}
