package com.example.comot.auth;

import com.example.comot.auth.application.services.jwtService.ConcreteJWTService;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.domaine.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JWTServiceTest {
    @Test
    void shouldTokenize() {
        JWTService jwtService = new ConcreteJWTService(
                "this_is_my_fake_secret_key_please_dont_share",
                60
        );

        var user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        var token = jwtService.tokenize(user);

        Assertions.assertNotNull(token);

        var authUser = jwtService.parse(token);
        Assertions.assertNotNull(authUser);
        Assertions.assertEquals(authUser.getId(), user.getId());
        Assertions.assertEquals(authUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(authUser.getLastname(), user.getLastname());
        Assertions.assertEquals(authUser.getEmail(), user.getEmail());

    }
}
