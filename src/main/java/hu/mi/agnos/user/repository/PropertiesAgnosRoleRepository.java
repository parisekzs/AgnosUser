/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.model.AgnosRole;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author parisek
 */
public class PropertiesAgnosRoleRepository {

    private String uri;

    public PropertiesAgnosRoleRepository(String path) {
        if (path.endsWith("/")) {
            this.uri = path + "application-roles.properties";
        } else {
            this.uri = path + "/application-roles.properties";
        }
    }

    public Set<AgnosRole> findAll() {
        Set<AgnosRole> result = new HashSet<>();
        try (InputStream input = new FileInputStream(this.uri)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            for (Object roles : prop.values()) {
                for (String role : ((String) roles).split(",")) {
                    result.add(new AgnosRole(role));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Set<AgnosRole> findAllByUser(String userName) {
//        long s1 = System.nanoTime();
        Set<AgnosRole> result = new HashSet<>();
        Set<AgnosRole> roles = findAll();

        try (InputStream input = new FileInputStream(this.uri)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            String rolesString = prop.getProperty(userName);
            if (rolesString != null) {
                for (String role : rolesString.split(",")) {
                    AgnosRole currentRole = new AgnosRole(role);

                    if (roles.contains(currentRole)) {
                        result.add(currentRole);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        long s2 = System.nanoTime();
        return result;
    }

    public AgnosRole findByName(String roleName) {
        AgnosRole result = null;

        try (InputStream input = new FileInputStream(this.uri)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            boolean isFind = false;

            for (Object roles : prop.values()) {
                for (String role : ((String) roles).split(",")) {
                    if (roleName.equals(role)) {
                        result = new AgnosRole(role);
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void saveUserRoles(String userName, String roles) {

        try (OutputStream output = new FileOutputStream(uri)) {

            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(userName, roles);

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    public void removeUserRoles(String userName) {

        try (OutputStream output = new FileOutputStream(uri)) {

            Properties prop = new Properties();
            // set the properties value
            prop.remove(userName);

            // save properties to project root folder
            prop.store(output, null);

//            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    
}
