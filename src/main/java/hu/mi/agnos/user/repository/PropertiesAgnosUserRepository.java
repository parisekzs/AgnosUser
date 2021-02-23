/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import hu.mi.agnos.user.model.AgnosUser;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parisek
 */
public class PropertiesAgnosUserRepository {

    private String path;
    private String uri;

    public PropertiesAgnosUserRepository(String path) {
        this.path = path;
        if (path.endsWith("/")) {
            this.uri = path + "application-users.properties";
        } else {
            this.uri = path + "/application-users.properties";
        }

    }

    
    public Set<AgnosUser> findAll() {
        Set<AgnosUser> result = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(uri))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String name = line.substring(0, line.indexOf('='));
                    String content = line.substring(line.indexOf('=') + 1);
                    AgnosUser user = new AgnosUser(name);
                    for (String s : content.split(",")) {
                        String[] segment = s.split(":");
                        String key = segment[0].trim();
                        switch (key) {
                            case "password":
                                user.setEncodedPassword(segment[1].trim());
                                break;
                            case "firsName":
                                user.setFirstName(segment[1].trim());
                                break;
                            case "lastName":
                                user.setLastName(segment[1].trim());
                                break;
                            case "email":
                                user.setEmail(segment[1].trim());
                                break;
                            case "isEnabled":
                                if (segment[1].trim().toUpperCase().equals("TRUE")) {
                                    user.setEnabled(true);
                                }
                                break;
                        }
                    }
                    user.setRoles(new PropertiesAgnosRoleRepository(path).findAllByUser(name));
                    result.add(user);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesAgnosUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public AgnosUser findByName(String userName) {
        AgnosUser result = null;
        try (InputStream input = new FileInputStream(this.uri)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            String content = prop.getProperty(userName);
            if (content != null) {
                result = new AgnosUser(userName);
                for (String s : content.split(",")) {
                    String[] segment = s.split(":");
                    String key = segment[0].trim();
                    switch (key) {
                        case "password":
                            if (segment.length == 2) {
                                result.setEncodedPassword(segment[1].trim());
                            }
                            break;
                        case "firsName":
                            if (segment.length == 2) {
                                result.setFirstName(segment[1].trim());
                            }
                            break;
                        case "lastName":
                            if (segment.length == 2) {
                                result.setLastName(segment[1].trim());
                            }
                            break;
                        case "email":
                            if (segment.length == 2) {
                                result.setEmail(segment[1].trim());
                            }
                            break;
                        case "isEnabled":
                            if (segment.length == 2) {
                                if (segment[1].trim().toUpperCase().equals("TRUE")) {
                                    result.setEnabled(true);
                                }
                            }
                            break;
                    }
                }
                result.setRoles(new PropertiesAgnosRoleRepository(path).findAllByUser(userName));
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesAgnosUserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void saveUserAttributes(String userName, String attributes) {

        try (OutputStream output = new FileOutputStream(uri)) {

            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(userName, attributes);

            // save properties to project root folder
            prop.store(output, null);

//            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    
    public void removeUserAttributes(String userName) {

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
