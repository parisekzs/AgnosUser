/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.converter;

import hu.mi.agnos.user.entity.dao.AgnosDAORole;
import hu.mi.agnos.user.entity.dto.AgnosDTORole;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author parisek
 */
public class AgnosRoleConverter {

    public static AgnosDAORole dto2dao(AgnosDTORole role) {
        return new AgnosDAORole(
                role.getName(),
                role.isPermanent(),
                role.getDescription()
        );
    }

    public static List<AgnosDAORole> dto2dao(List<AgnosDTORole> dtoRoles) {
        List<AgnosDAORole> result = new ArrayList<>();
        if (!dtoRoles.isEmpty()) {

            for (AgnosDTORole role : dtoRoles) {

                AgnosDAORole daoRole = new AgnosDAORole(
                        role.getName(),
                        role.isPermanent(),
                        role.getDescription()
                );
                result.add(daoRole);
            }
        }
        return result;
    }

    public static Optional<AgnosDTORole> dao2dto(AgnosDAORole role) {
        if (role == null) {
            return Optional.empty();
        } else {
            AgnosDTORole dtoRole = new AgnosDTORole(
                    role.getName(),
                    role.isPermanent(),
                    role.getDescription()
            );
            return Optional.ofNullable(dtoRole);
        }
    }

    public static Optional<AgnosDTORole> dao2dto(Optional<AgnosDAORole> daoRole) {
        if (daoRole.isEmpty()) {
            return Optional.empty();
        } else {
            AgnosDAORole role = daoRole.get();
            AgnosDTORole dtoRole = new AgnosDTORole(
                    role.getName(),
                    role.isPermanent(),
                    role.getDescription()
            );
            return Optional.ofNullable(dtoRole);
        }
    }

    public static List<AgnosDTORole> dao2dto(List<AgnosDAORole> daoRoles) {
        List<AgnosDTORole> result = new ArrayList<>();
        if (!daoRoles.isEmpty()) {

            for (AgnosDAORole role : daoRoles) {

                AgnosDTORole dtoRole = new AgnosDTORole(
                        role.getName(),
                        role.isPermanent(),
                        role.getDescription()
                );
                result.add(dtoRole);
            }
        }
        return result;
    }

}
