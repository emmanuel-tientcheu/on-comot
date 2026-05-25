package com.example.comot.role.infrastructure.spring.configuration;

import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleQueries;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleRepository;
import com.example.comot.role.infrastructure.persistance.sql.SQLRoleQueries;
import com.example.comot.role.infrastructure.persistance.sql.SQLRoleRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleAdapterConfiguration {

    @Bean
    public RoleRepository roleRepository(EntityManager entityManager) {
        return new SQLRoleRepository(entityManager);
    }

    @Bean
    public RoleQueries roleQueries(EntityManager manager) { return new SQLRoleQueries(manager); }
}
