package com.example.comot.auth.application.services.passwordHasher;

public interface PasswordHasher {
    public String encode(String password);
    public boolean match(String clearPassword, String hashPassword);
}
