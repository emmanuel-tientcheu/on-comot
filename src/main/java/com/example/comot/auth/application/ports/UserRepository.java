package com.example.comot.auth.application.ports;

import com.example.comot.auth.domaine.model.User;

import java.util.Optional;

public interface UserRepository {
    public Optional<User> findById(String id);
    public void save(User user);
    public void delete(User user);
    public Optional<User> findByEmail(String email);
    public void clear();
}
