package com.syphan.pwebproject.service;

import java.util.List;

public interface GenericService<T> {

    T create(T obj) throws Exception;

    void delete(long id);

    List<T> findAll();

    T findById(long id);
}
