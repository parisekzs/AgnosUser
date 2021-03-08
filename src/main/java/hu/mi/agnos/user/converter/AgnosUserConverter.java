/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.converter;

import hu.mi.agnos.user.entity.dao.AgnosDAOUser;
import hu.mi.agnos.user.entity.dto.AgnosDTOUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.util.Assert;

/**
 *
 * @author parisek
 */
public class AgnosUserConverter {

    public static AgnosDAOUser dto2dao(AgnosDTOUser dtoUser) {
            return new AgnosDAOUser(
                    dtoUser.getName(),
                    dtoUser.getEmail(),
                    dtoUser.getRealName(),
                    dtoUser.getEncodedPassword(),
                    dtoUser.getRoles(),
                    dtoUser.isPermanent()
            );        
    }

    public static List<AgnosDAOUser> dto2dao(List<AgnosDTOUser> dtoUsers) {
        List<AgnosDAOUser> result = new ArrayList<>();
        if (!dtoUsers.isEmpty()) {

            for (AgnosDTOUser user : dtoUsers) {

                AgnosDAOUser daoUser = new AgnosDAOUser(
                        user.getName(),
                        user.getEmail(),
                        user.getRealName(),
                        user.getEncodedPassword(),
                        user.getRoles(),
                        user.isPermanent()
                );
                result.add(daoUser);
            }
        }
        return result;
    }

    public static Optional<AgnosDTOUser> dao2dto(Optional<AgnosDAOUser> daoUser) {
        if (daoUser.isEmpty()) {
            return Optional.empty();
        } else {
            AgnosDAOUser user = daoUser.get();
            AgnosDTOUser dtoUser = new AgnosDTOUser(
                    user.getName(),
                    user.getEmail(),
                    user.getRealName(),
                    user.getEncodedPassword(),
                    user.getRoles(),
                    user.isPermanent()
            );
            return Optional.ofNullable(dtoUser);
        }
    }

    public static List<AgnosDTOUser> dao2dto(List<AgnosDAOUser> daoUsers) {
        List<AgnosDTOUser> result = new ArrayList<>();
        if (!daoUsers.isEmpty()) {

            for (AgnosDAOUser user : daoUsers) {

                AgnosDTOUser dtoUser = new AgnosDTOUser(
                        user.getName(),
                        user.getEmail(),
                        user.getRealName(),
                        user.getEncodedPassword(),
                        user.getRoles(),
                        user.isPermanent()
                );
                result.add(dtoUser);
            }
        }
        return result;
    }

}
