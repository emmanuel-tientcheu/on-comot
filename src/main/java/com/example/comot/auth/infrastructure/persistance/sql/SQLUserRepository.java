package com.example.comot.auth.infrastructure.persistance.sql;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SQLUserRepository implements UserRepository {
    private final EntityManager entity;
    private final SQLUserAccessor dataAccessor;

    public SQLUserRepository(EntityManager entity, SQLUserAccessor dataAccessor) {
        this.entity = entity;
        this.dataAccessor = dataAccessor;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(this.entity.find(User.class, id));
    }

    @Override
    public void save(User user) {
        this.entity.persist(user);
    }

    @Override
    public void delete(User user) {
        this.entity.remove(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.dataAccessor.findByEmail(email);
    }

    @Override
    public void clear() {
        this.entity.createQuery("DELETE FROM " + User.class.getSimpleName()).executeUpdate();
    }
}
