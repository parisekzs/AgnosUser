/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.agnos.user.entity.AbstractDAOEntity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public abstract class PropertyRepository<T extends AbstractDAOEntity, ID extends Serializable> implements IPropertyRepository<T, ID> {

    protected static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    protected final Logger logger;

    protected String uri;
    protected final String path;

    private Class<T> entityClass;
            
    public PropertyRepository(Class<T> entityClass, String path) {
        this.entityClass = entityClass;
        logger = LoggerFactory.getLogger(entityClass);
        this.path = new StringBuilder(path)
                .append(path.endsWith("/") ? "" : "/")
                .toString();
        init();
    }

    @PostConstruct
    protected abstract void init();

    @Override
    public Optional<T> deleteById(ID id) {
        Optional<T> deleted = findById(id);

        if (deleted.isPresent()) {
            try (OutputStream output = new FileOutputStream(uri)) {

                Properties prop = new Properties();
                // set the properties value
                prop.remove((String) id);

                // save properties to project root folder
                prop.store(output, null);
                return deleted;
            } catch (IOException io) {
                logger.error(io.getMessage());
                return Optional.empty();
            }
        }
        return deleted;
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        T result = null;
        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            String valueString = prop.getProperty((String) id);
            if (valueString != null) {
                result = parseEntityFromJSONString((String) id, valueString);
            }
            return Optional.ofNullable(result);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>();

        try (FileInputStream input = new FileInputStream(this.uri)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(new InputStreamReader(input, Charset.forName("UTF-8")));

            for (Object key : prop.keySet()) {
                String valueString = prop.getProperty((String) key);
                if (valueString != null) {
                    result.add(parseEntityFromJSONString((String) key, valueString));
                }
            }
        } catch (IOException io) {
            logger.error(io.getMessage());
        }
        return result;
    }

    @Override
    public Optional<T> save(T entity) {

        Assert.notNull(entity, "Entity must not be null.");

        try (OutputStream output = new FileOutputStream(uri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String value = mapper.writeValueAsString(entity);

            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(entity.getName(), value);

            // save properties to project root folder
            prop.store(output, null);
            return Optional.ofNullable(entity);
        } catch (IOException io) {
            logger.error(io.getMessage());
            return Optional.empty();
        }
    }

    protected T parseEntityFromJSONString(String key, String value) {
        T result = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            result = mapper.readValue(value, entityClass);
            result.setName(key);

        } catch (JsonProcessingException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

}
