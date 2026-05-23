package com.example.comot.auth.infrastructure.spring.configuration;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.auth.infrastructure.persistance.sql.SQLUserAccessor;
import com.example.comot.auth.infrastructure.persistance.sql.SQLUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthAdapterConfiguration {
    @Bean
    UserRepository userRepository(EntityManager entityManager, SQLUserAccessor userAccessor) {
        return new SQLUserRepository(entityManager, userAccessor);
    }
}
