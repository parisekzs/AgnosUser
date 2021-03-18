/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import hu.mi.user.properties.entity.UserRoles;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author parisek
 */
public class UserRolesRepository extends AbstractPropertyRepo<UserRoles, String> {

    public UserRolesRepository(String path) {
        super(UserRoles.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
        this.uri = new StringBuilder(this.path)
                .append("auth/application-users-roles.properties")
                .toString();
    }

    public List<UserRoles> findAllByRoleName(String roleName) {
        List<UserRoles> result = new ArrayList<>();
        
        for(UserRoles userRoles : findAll()){
            if(userRoles.getRoles().contains(roleName)){
                result.add(userRoles);
            }
        } 
        return result;
    }
}
