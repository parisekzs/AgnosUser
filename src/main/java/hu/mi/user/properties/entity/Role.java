/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import lombok.AllArgsConstructor;

/**
 *
 * @author parisek
 */
public class Role extends AbstractEntity {

    private boolean permanent;
    private String description;

    public Role() {
        super();
    }

    public Role(String name) {
        super(name);
    }    

    public Role( String name, boolean permanent, String description) {
        super(name);
        this.permanent = permanent;
        this.description = description;
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
