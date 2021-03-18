/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.properties.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.mi.user.properties.entity.AbstractEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

public abstract class AbstractPropertyRepo<T extends AbstractEntity, ID extends String> implements CrudRepository<T, ID> {

    protected static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
    protected final Logger logger;

    protected String uri;
    protected String tmpUri;

    protected final String path;

    private Class<T> entityClass;

    public AbstractPropertyRepo(Class<T> entityClass, String path) {
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
    public void deleteById(ID id) {
        Optional<T> optEntity = findById(id);
        if (optEntity.isPresent()) {
            T entity = optEntity.get();
            List<T> storedEntities = findAll();
            storedEntities.remove(entity);
            storeToFile(storedEntities);
        }
    }

    @Override
    public void delete(T t) {
        deleteById((ID) t.getName());
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        List<T> storedEntities = findAll();
        for (T entity : entities) {
            storedEntities.remove(entity);
        }
        storeToFile(storedEntities);
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported!");
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
    public Iterable<T> findAllById(Iterable<ID> entityIds) {
        List<T> result = new ArrayList<>();
        for (ID id : entityIds) {
            Optional<T> optEntity = findById(id);
            if (optEntity.isPresent()) {
                result.add(optEntity.get());
            }
        }
        return result;
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
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        List<T> storedEntities = findAll();
        if (storedEntities.contains(entity)) {
            storedEntities.remove(entity);
        }
        storedEntities.add(entity);
        storeToFile(storedEntities);
        return entity;

    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();

        List<T> storedEntities = findAll();

        for (S entity : entities) {
            if (storedEntities.contains(entity)) {
                storedEntities.remove(entity);
            }
            storedEntities.add(entity);
            result.add(entity);
        }
        storeToFile(storedEntities);
        return result;
    }

    @Override
    public boolean existsById(ID id) {
        return this.findById(id).isPresent();
    }

    @Override
    public long count() {
        return this.findAll().size();
    }

    protected boolean storeToFile(List<T> entities) {
        try (OutputStream output = new FileOutputStream(tmpUri)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            Properties prop = new Properties();
            //copy all property to new file
            for (T entity : entities) {
                String value = mapper.writeValueAsString(entity);
                prop.setProperty(entity.getName(), value);
            }
            prop.store(output, null);

        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
            return false;
        } catch (IOException io) {
            logger.error(io.getMessage());
            return false;
        }

        File tmpfile = new File(tmpUri);
        tmpfile.renameTo(new File(uri));
        return true;
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
