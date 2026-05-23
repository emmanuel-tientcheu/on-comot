package com.example.comot.auth.infrastructure.persistance.sql;

import com.example.comot.auth.domaine.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SQLUserAccessor extends CrudRepository<User, String> {
    public Optional<User> findByEmail(String email);
}
