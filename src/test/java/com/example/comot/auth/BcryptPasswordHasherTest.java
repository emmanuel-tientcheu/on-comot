package com.example.comot.auth;

import com.example.comot.auth.application.services.passwordHasher.BcryptPasswordHasher;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BcryptPasswordHasherTest {

    @Test
    void shouldEncodePassword() {
        PasswordHasher hasher = new BcryptPasswordHasher();
        var clearPassword = "password";

        var encodedPassword = hasher.encode(clearPassword);

        Assertions.assertTrue(
                hasher.match(clearPassword, encodedPassword)
        );
    }
}
