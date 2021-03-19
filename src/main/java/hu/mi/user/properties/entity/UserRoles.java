/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author parisek
 */
public class UserRoles extends AbstractEntity {

    private List<String> roles;

    public UserRoles() {
    }

    public UserRoles(User user) {
        this.roles = user.getRoles();
        this.name = user.getName();
    }

    @JsonIgnore
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AgnosUserRoles{" + "name=" + name + ", roles=" + roles + '}';
    }

}
