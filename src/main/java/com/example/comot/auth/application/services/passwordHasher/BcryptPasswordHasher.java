package com.example.comot.auth.application.services.passwordHasher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordHasher implements PasswordHasher{
     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return this.encoder.encode(password);
    }

    @Override
    public boolean match(String clearPassword, String hashPassword) {
        return this.encoder.matches(clearPassword, hashPassword);
    }
}
