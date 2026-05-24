package com.example.comot.auth.infrastructure.persistance.ram;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        return this.entities.values().stream().filter(
                user -> user.getEmail().equals(email)
        ).findFirst();
    }

}
