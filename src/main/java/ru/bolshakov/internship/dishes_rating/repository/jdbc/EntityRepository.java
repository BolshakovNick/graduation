package ru.bolshakov.internship.dishes_rating.repository.jdbc;

import ru.bolshakov.internship.dishes_rating.model.jdbc.BaseEntity;

import java.util.List;

public interface EntityRepository<T extends BaseEntity> {

    T save(T entity);

    boolean delete(Long id);

    T get(Long id);

    List<T> getAll();
}
