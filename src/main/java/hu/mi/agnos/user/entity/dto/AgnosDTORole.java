/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author parisek
 */
@Getter
@Setter
@AllArgsConstructor
public class AgnosDTORole {

    private String name;
    private boolean permanent;
    private String description;  
}
