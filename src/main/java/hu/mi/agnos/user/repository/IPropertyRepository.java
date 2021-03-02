/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.user.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IPropertyRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> deleteById(ID id);
    
    public Optional<T> findById(ID id);
    
    public List<T> findAll();
    
    public Optional<T> save(T entity);

}
