package com.example.comot.core.application.ports;

import com.example.comot.core.domaine.model.BaseEntity;

import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
    public Optional<T> findById(String id);
    public void save(T entity);
    public void delete(T entity);
    public void clear();
}
