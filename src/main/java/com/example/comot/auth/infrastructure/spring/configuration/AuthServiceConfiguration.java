package com.example.comot.auth.infrastructure.spring.configuration;

import com.example.comot.auth.application.services.jwtService.ConcreteJWTService;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.application.services.passwordHasher.BcryptPasswordHasher;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceConfiguration {

    @Bean
    PasswordHasher passwordHasher() {
        return new BcryptPasswordHasher();
    }

    @Bean
    JWTService jwtService() {
        return new ConcreteJWTService(
                "this_is_my_fake_secret_key_please_dont_share",
                3600
        );
    }
}
