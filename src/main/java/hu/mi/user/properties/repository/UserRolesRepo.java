/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.entity.UserRoles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.util.Assert;

/**
 *
 * @author parisek
 */
public class UserRolesRepo extends AbstractPropertyRepo<UserRoles, String> {

    public UserRolesRepo(String path) {
        super(UserRoles.class, path);
    }

    @PostConstruct
    @Override
    protected void init() {
        this.uri = new StringBuilder(this.path)
                .append("auth/application-users-roles.properties")
                .toString();
        this.tmpUri = new StringBuilder(this.path)
                .append("auth/tmp-application-users-roles.properties")
                .toString();

    }

    public UserRoles save(Role role) {
        Assert.notNull(role, "Role must not be null.");
        List<UserRoles> storedEntities = findAll();
        for (String userName : role.getUsers()) {
            User fakeUser = new User();
            fakeUser.setName(userName);
            UserRoles fakeUserRoles = new UserRoles(fakeUser);
            if (storedEntities.contains(fakeUserRoles)) {
                UserRoles ur = storedEntities.get(storedEntities.indexOf(fakeUserRoles));
                if (!ur.getRoles().contains(role.getName())) {
                    ur.getRoles().add(role.getName());
                }
            } else {
                UserRoles newUserRole = new UserRoles(fakeUser);
                newUserRole.setRoles(new ArrayList<>(Arrays.asList(role.getName())));            
                storedEntities.add(newUserRole);
            }
        }
        storeToFile(storedEntities);
        return null;
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
