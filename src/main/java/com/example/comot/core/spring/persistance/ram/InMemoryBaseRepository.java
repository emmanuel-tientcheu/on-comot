package com.example.comot.core.spring.persistance.ram;

import com.example.comot.core.application.ports.BaseRepository;
import com.example.comot.core.domaine.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryBaseRepository<T extends BaseEntity> implements BaseRepository<T> {
    protected Map<String, T> entities = new HashMap<>();

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(this.entities.get(id));
    }

    @Override
    public void save(T entity) {
        this.entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        this.entities.remove(entity.getId());
    }

    @Override
    public void clear() {
        this.entities.clear();
    }
}
