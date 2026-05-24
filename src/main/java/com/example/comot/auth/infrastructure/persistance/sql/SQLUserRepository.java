package com.example.comot.auth.infrastructure.persistance.sql;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.core.spring.persistance.sql.SQLBaseRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SQLUserRepository extends SQLBaseRepository<User> implements UserRepository {
    private final SQLUserAccessor dataAccessor;

    public SQLUserRepository(EntityManager entity, SQLUserAccessor dataAccessor) {
        super(entity);
        this.dataAccessor = dataAccessor;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.dataAccessor.findByEmail(email);
    }

    @Override
    public Class<User> getEntityMangerClass() {
        return User.class;
    }
}
