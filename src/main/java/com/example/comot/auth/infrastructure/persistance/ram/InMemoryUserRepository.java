package com.example.comot.auth.infrastructure.persistance.ram;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(this.users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.users.values().stream().filter(
                user -> user.getEmail().equals(email)
        ).findFirst();
    }

    @Override
    public void save(User user) {
        this.users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        this.users.remove(user.getId());
    }

    @Override
    public void clear() {
        this.users.clear();
    }
}
