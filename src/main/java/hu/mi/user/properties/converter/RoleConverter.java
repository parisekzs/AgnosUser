/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.converter;

import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.model.RoleDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author parisek
 */
public class RoleConverter {

    public static Role dto2dao(RoleDTO role) {
        return new Role(
                role.getName(),
                role.isPermanent(),
                role.getDescription(),
                role.getUsers()
        );
    }

    public static List<Role> dto2dao(List<RoleDTO> dtoRoles) {
        List<Role> result = new ArrayList<>();
        if (!dtoRoles.isEmpty()) {

            for (RoleDTO role : dtoRoles) {

                Role daoRole = new Role(
                        role.getName(),
                        role.isPermanent(),
                        role.getDescription(),
                        role.getUsers()
                );
                result.add(daoRole);
            }
        }
        return result;
    }

    public static Optional<RoleDTO> dao2dto(Role role) {
        if (role == null) {
            return Optional.empty();
        } else {
            RoleDTO dtoRole = new RoleDTO(
                    role.getName(),
                    role.isPermanent(),
                    role.getDescription(),
                    role.getUsers()
            );
            return Optional.ofNullable(dtoRole);
        }
    }

    public static Optional<RoleDTO> dao2dto(Optional<Role> daoRole) {
        if (daoRole.isEmpty()) {
            return Optional.empty();
        } else {
            Role role = daoRole.get();
            RoleDTO dtoRole = new RoleDTO(
                    role.getName(),
                    role.isPermanent(),
                    role.getDescription(),
                    role.getUsers()
            );
            return Optional.ofNullable(dtoRole);
        }
    }

    public static List<RoleDTO> dao2dto(List<Role> daoRoles) {
        List<RoleDTO> result = new ArrayList<>();
        if (!daoRoles.isEmpty()) {

            for (Role role : daoRoles) {

                RoleDTO dtoRole = new RoleDTO(
                        role.getName(),
                        role.isPermanent(),
                        role.getDescription(),
                        role.getUsers()
                );
                result.add(dtoRole);
            }
        }
        return result;
    }

}
