/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.converter;

import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.model.UserDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author parisek
 */
public class UserConverter {

    public static User dto2dao(UserDTO dtoUser) {
        User daoUser = new User(
                dtoUser.getName(),
                dtoUser.getEmail(),
                dtoUser.getRealName(),
                dtoUser.getRoles(),
                dtoUser.isPermanent()
        );
        daoUser.setPlainPassword(dtoUser.getPlainPassword());
        return daoUser;
    }

    public static List<User> dto2dao(List<UserDTO> dtoUsers) {
        List<User> result = new ArrayList<>();
        if (!dtoUsers.isEmpty()) {

            for (UserDTO user : dtoUsers) {

                User daoUser = new User(
                        user.getName(),
                        user.getEmail(),
                        user.getRealName(),
                        user.getRoles(),
                        user.isPermanent()
                );
                daoUser.setPlainPassword(user.getPlainPassword());
                result.add(daoUser);
            }
        }
        return result;
    }

    public static Optional<UserDTO> dao2dto(Optional<User> daoUser) {
        if (daoUser.isEmpty()) {
            return Optional.empty();
        } else {
            User user = daoUser.get();
            UserDTO dtoUser = new UserDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getRealName(),
                    "",
                    user.getRoles(),
                    user.isPermanent()
            );
            return Optional.ofNullable(dtoUser);
        }
    }

        public static Optional<UserDTO> dao2dto(User user) {
        if (user == null) {
            return Optional.empty();
        } else {
            UserDTO dtoUser = new UserDTO(
                    user.getName(),
                    user.getEmail(),
                    user.getRealName(),
                    "",
                    user.getRoles(),
                    user.isPermanent()
            );
            return Optional.ofNullable(dtoUser);
        }
    }

    
    public static List<UserDTO> dao2dto(List<User> daoUsers) {
        List<UserDTO> result = new ArrayList<>();
        if (!daoUsers.isEmpty()) {

            for (User user : daoUsers) {

                UserDTO dtoUser = new UserDTO(
                        user.getName(),
                        user.getEmail(),
                        user.getRealName(),
                        "",
                        user.getRoles(),
                        user.isPermanent()
                );
                result.add(dtoUser);
            }
        }
        return result;
    }

}
