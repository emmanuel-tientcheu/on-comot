package com.example.comot.permission.infrastructure.spring.configuration;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.application.useCases.CreatePermissionCommandHandler;
import com.example.comot.permission.application.useCases.GetPermissionsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionUseCaseConfiguration {
    @Bean
    public CreatePermissionCommandHandler createPermissionCommandHandler(PermissionRepository permissionRepository) {
        return new CreatePermissionCommandHandler(permissionRepository);
    }

    @Bean
    public GetPermissionsHandler getPermissionsHandler(PermissionRepository permissionRepository) {
        return new GetPermissionsHandler(permissionRepository);
    }
}
