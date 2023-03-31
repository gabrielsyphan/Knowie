package com.syphan.pwebproject.service;

import java.util.List;

public interface GenericService<T, U> {

    T create(T obj) throws Exception;

    void delete(U obj);

    List<T> findAll();

    T findById(long id);
}
