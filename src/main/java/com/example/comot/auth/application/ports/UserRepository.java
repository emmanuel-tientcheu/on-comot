package com.example.comot.auth.application.ports;

import com.example.comot.auth.domaine.model.User;
import com.example.comot.core.application.ports.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    public Optional<User> findByEmail(String email);
    public void clear();
}
