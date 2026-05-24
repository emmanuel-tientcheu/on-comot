package com.example.comot.core.spring.persistance.sql;

import com.example.comot.core.application.ports.BaseRepository;
import com.example.comot.core.domaine.model.BaseEntity;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public abstract class SQLBaseRepository<T extends BaseEntity> implements BaseRepository<T> {
    protected final EntityManager entityManager;

    public SQLBaseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(this.entityManager.find(getEntityMangerClass(), id));
    }

    @Override
    public void save(BaseEntity entity) {
        this.entityManager.persist(entity);
    }

    @Override
    public void delete(BaseEntity entity) {
        this.entityManager.remove(entity);
    }

    @Override
    public void clear() {
        this.entityManager.createQuery("DELETE FROM "+ getEntityMangerClass().getSimpleName()).executeUpdate();
    }

    public abstract Class<T> getEntityMangerClass();
}
