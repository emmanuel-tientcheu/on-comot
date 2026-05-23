package com.example.comot.auth.infrastructure.spring.configuration;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.application.useCases.LoginCommandHandler;
import com.example.comot.auth.application.useCases.RegisterCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfiguration {
    @Bean
    RegisterCommandHandler registerCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new RegisterCommandHandler(userRepository, passwordHasher);
    }

    @Bean
    LoginCommandHandler loginCommandHandler(UserRepository userRepository, PasswordHasher passwordHasher, JWTService jwtService) {
        return new LoginCommandHandler(userRepository, passwordHasher, jwtService);
    }
}
