/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author parisek
 */
public class Role extends AbstractEntity {

    private boolean permanent;
    private String description;
    private List<String> users;

    public Role() {
        super();
        this.users = new ArrayList<>();
    }

    public Role(String name) {
        super(name);
        this.users = new ArrayList<>();
    }    

    public Role( String name, boolean permanent, String description, List<String> users) {
        super(name);
        this.permanent = permanent;
        this.description = description;
        this.users = users;
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

    @JsonIgnore
    public List<String> getUsers() {
        return users;
    }    

    public void setUsers(List<String> users) {
        this.users = users;
    }
    
    public void addUser(String userName) {
        if (!this.users.contains(userName)) {
            this.users.add(userName);
        }
        Collections.sort(this.users);
    }
    
    public boolean hasUser(String userName) {
        return this.users.contains(userName);
    }    
    
    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 
    @Override
    public String toString() {
        return "AgnosRole{" + "name=" + name + ", permanent=" + permanent + ", description=" + description + '}';
    }

    
}
