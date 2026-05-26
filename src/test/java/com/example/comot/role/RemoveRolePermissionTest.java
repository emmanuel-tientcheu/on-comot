package com.example.comot.role;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.application.useCases.RemoveRolePermissionCommand;
import com.example.comot.role.application.useCases.RemoveRolePermissionCommandHandler;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoveRolePermissionTest {

    private final RoleRepository roleRepository = new InMemoryRoleRepository();
    private final PermissionRepository permissionRepository = new InMemoryPermissionRepository();

    Role role;
    Permission permission;

    @BeforeEach
    void setUp() {
        permissionRepository.clear();
        roleRepository.clear();

        permission = new Permission(
                "permission-1",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        permissionRepository.save(permission);

        role = new Role(
                "role-1",
                "title",
                "description",
                true
        );

        role.addPermission(permission.getId(), permission.getCategory());
        roleRepository.save(role);
    }

    @Test
    void shouldRemoveRole() {
        var command = new RemoveRolePermissionCommand(
                role.getId(),
                permission.getId()
        );

        var useCase = new RemoveRolePermissionCommandHandler(roleRepository, permissionRepository);
        useCase.handle(command);

        Assertions.assertFalse(role.hasPermission(permission.getCategory()));
    }

    @Test
    void whenRoleDoesNotAssign_shouldThrow() {
        var  permission = new Permission(
                "permission-2",
                "title",
                Category.MANAGE_SCHEDULE,
                "description"
        );
        this.permissionRepository.save(permission);

        var command = new RemoveRolePermissionCommand(
                role.getId(),
                permission.getId()
        );

        var useCase = new RemoveRolePermissionCommandHandler(roleRepository, permissionRepository);

        var exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->useCase.handle(command)
        );

        Assertions.assertEquals("This permission does not exist in this role", exception.getMessage());

    }
}
