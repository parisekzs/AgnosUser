/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.entity.UserRoles;
import java.util.List;
import java.util.Optional;
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
                .append("auth/application-roles.properties")
                .toString();
        this.tmpUri = new StringBuilder(this.path)
                .append("auth/tmp-application-roles.properties")
                .toString();

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
