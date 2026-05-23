package com.example.comot.auth.application.services.jwtService;

import com.example.comot.auth.domaine.model.AuthUser;
import com.example.comot.auth.domaine.model.User;

public interface JWTService {
    public String tokenize(User user);
    public AuthUser parse(String token);
}
