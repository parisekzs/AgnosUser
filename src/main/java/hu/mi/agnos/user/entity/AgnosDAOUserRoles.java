/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

/**
 *
 * @author parisek
 */
public class AgnosDAOUserRoles extends AbstractDAOEntity{

    private String name;
    private ArrayList<String> roles;

    public AgnosDAOUserRoles() {
    }

    
    public AgnosDAOUserRoles(AgnosDAOUser user) {
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

    public ArrayList<String> getRoles() {
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
