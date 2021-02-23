/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 *
 * @author parisek
 */
@Getter
@Setter
public class AgnosUser {

    private String name;
    private String email;
    private String firstName;
    private String lastName;
    private String encodedPassword;
    private Set<AgnosRole> roles;
    private boolean enabled;

    public AgnosUser(String name) {
        this.name = name;
        this.roles = new HashSet<>();
        this.enabled = false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AgnosUser other = (AgnosUser) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public String getUserRolesAsString() {
        StringBuilder result = new StringBuilder();
        for (AgnosRole role : this.roles) {
            result.append(role.toString())
                    .append(",");
        }
        if (result.toString().endsWith(",")) {
            result = new StringBuilder(result.subSequence(0, result.length() - 1));
        }
        return result.toString();
    }

    public String getUsetAttributesAsString() {
        return new StringBuilder()
                .append("password:")
                .append(StringUtils.hasText(this.encodedPassword) ? this.encodedPassword : "")
                .append(" ,firsName:")
                .append(StringUtils.hasText(this.firstName) ? this.firstName : "")
                .append(" ,lastName:")
                .append(StringUtils.hasText(this.lastName) ? this.lastName : "")
                .append(" ,email:")
                .append(StringUtils.hasText(this.email) ? this.email : "")
                .append(" ,isEnabled:")
                .append(this.enabled)
                .toString();
    }

    @Override
    public String toString() {
        return "AgnosUser{" + "name=" + name + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", encodedPassword=" + encodedPassword + ", roles=" + roles + ", enabled=" + enabled + '}';
    }

}
