package com.example.comot.role;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.application.useCases.AddRolePermissionCommand;
import com.example.comot.role.application.useCases.AddRolePermissionCommandHandler;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleQueries;
import com.example.comot.role.infrastructure.persistance.ram.InMemoryRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddRolePermissionTest {
    private final RoleRepository repository = new InMemoryRoleRepository();
    private final PermissionRepository permissionRepository = new InMemoryPermissionRepository();

    private AddRolePermissionCommandHandler commandHandler() {
        return new AddRolePermissionCommandHandler(repository, permissionRepository);
    }

    Role role;

    @BeforeEach
    void setUp() {
        repository.clear();
        permissionRepository.clear();
        for (Category category: Category.values()) {
            var permission = new Permission(
                    "permission_" + category.toString(),
                    "title",
                    category,
                    "description"
            );

            permissionRepository.save(permission);
        }

        role = new Role(
                "role-1",
                "title",
                "description",
                true
        );

        this.repository.save(role);
    }

    @Test
    void shouldAddRole() {
        var permission = permissionRepository.getAllPermissions().getFirst();
        
        var command = new AddRolePermissionCommand(
                role.getId(),
                permission.getId()
        );

        var useCase = commandHandler();

        var roleViewModel = useCase.handle(command);

        Assertions.assertTrue(role.hasPermission(permission.getCategory()));

    }

    @Test
    void whenAddRoleAlreadyAssigned_shouldThrow() {
        var permission = permissionRepository.getAllPermissions().getFirst();

        role.addPermission(permission.getId(), permission.getCategory());

        var command = new AddRolePermissionCommand(
                role.getId(),
                permission.getId()
        );

        var useCase = commandHandler();

        Assertions.assertThrows(
                IllegalArgumentException.class,
                ()-> useCase.handle(command)
        );
    }

    @Test
    void whenPermissionDoesNotExist_shouldThrow() {
        var command = new AddRolePermissionCommand(
                role.getId(),
                "garbage"
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                NotFoundException.class,
                ()-> useCase.handle(command)
        );

        Assertions.assertEquals("Permission with the key garbage was not found", exception.getMessage());
    }

    @Test
    void whenRoleDoesNotExist_shouldThrow() {
        var permission = permissionRepository.getAllPermissions().getFirst();

        role.addPermission(permission.getId(), permission.getCategory());

        var command = new AddRolePermissionCommand(
                "garbage",
                permission.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                NotFoundException.class,
                ()-> useCase.handle(command)
        );

        Assertions.assertEquals("Role with the key garbage was not found", exception.getMessage());
    }
}
