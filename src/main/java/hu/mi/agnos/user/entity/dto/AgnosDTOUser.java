/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.entity.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author parisek
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgnosDTOUser {

    private String name;
    private String email;
    private String realName;
    private String plainPassword;
    private ArrayList<String> roles;
    private boolean permanent;
      
}

