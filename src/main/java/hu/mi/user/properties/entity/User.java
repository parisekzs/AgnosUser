/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author parisek
 */
public class User extends AbstractEntity {

    private String email;
    private String realName;
    private String encodedPassword;
    private ArrayList<String> roles;
    private boolean permanent;

    public User() {
        this.roles = new ArrayList<>();
    }

    public User(String name, String email, String realName, ArrayList<String> roles, boolean permanent) {
        super(name);
        this.email = email;
        this.realName = realName;
        this.roles = roles;
        this.permanent = permanent;
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

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public String getRoleNamesString() {
        String result = "";
        String separator = "";
        for (String role : this.roles) {
            result = result + separator + role;
            separator = ", ";
        }
        return result;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

//
//    public String getSaltString() {
//        return Base64.getEncoder().encodeToString(salt);
//    }
//
//    public void setSaltFromString(String s) {
//        this.salt = Base64.getDecoder().decode(s);
//    }
    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public void setPlainPassword(String plainPassword) {
        if (plainPassword.length() > 0) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.encodedPassword = passwordEncoder.encode(plainPassword);
        }
    }

    @JsonIgnore
    public ArrayList<String> getRoles() {
        return roles;
    }

    public void addRole(String roleName) {
        if (!this.roles.contains(roleName)) {
            this.roles.add(roleName);
        }
        Collections.sort(this.roles);
    }

    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }


    @Override
    public String toString() {
        return "AgnosUser{" + "name=" + name + ", roles=" + roles + '}';
    }

}
