/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.model;

import java.util.List;
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
public class RoleDTO {

    private String name;
    private boolean permanent;
    private String description;
    private List<String> users;
}
