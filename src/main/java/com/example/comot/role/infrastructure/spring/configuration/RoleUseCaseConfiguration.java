package com.example.comot.role.infrastructure.spring.configuration;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.application.useCases.AddRolePermissionCommandHandler;
import com.example.comot.role.application.useCases.CreateRoleCommandHandler;
import com.example.comot.role.application.useCases.GetRoleCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleUseCaseConfiguration {
    @Bean
    public CreateRoleCommandHandler createRoleCommandHandler(RoleRepository roleRepository) {
        return new CreateRoleCommandHandler(roleRepository);
    }

    @Bean
    public AddRolePermissionCommandHandler addRolePermissionCommandHandler(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
       return new AddRolePermissionCommandHandler(roleRepository, permissionRepository);
    }

    @Bean
    public GetRoleCommandHandler getRoleCommandHandler(RoleQueries roleQueries) {
        return new GetRoleCommandHandler(roleQueries);
    }
}
