package com.example.comot.permission.infrastructure.spring.configuration;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import com.example.comot.permission.infrastructure.persistance.sql.SQLPermissionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionAdapterConfiguration {
    @Bean
    PermissionRepository permissionRepository(EntityManager entityManager) {
        return new SQLPermissionRepository(entityManager);
    }
}
