package com.congress.services;

import java.util.List;

public interface CrudService<T> {
    List<T> findAll();

    T findById(long id) throws Exception;

    T create(T entity);

    T update(long id, T entity) throws Exception;

    T update(T entity) throws Exception;

    void delete(long id) throws Exception;

}
