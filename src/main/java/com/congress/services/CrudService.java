package com.congress.services;

import com.congress.entity.Congress;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudService<T> {
    List<T> findAll();

    ResponseEntity<T> findById(long id) throws Exception;

    ResponseEntity<Congress> create(Congress congress);

    ResponseEntity<T> update(long id, T entity);

    ResponseEntity<T> delete(long id) throws Exception;

}
