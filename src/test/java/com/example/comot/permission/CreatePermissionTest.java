package com.example.comot.permission;

import com.example.comot.permission.application.exceptions.PermissionAlreadyExistsException;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.application.useCases.CreatePermissionCommand;
import com.example.comot.permission.application.useCases.CreatePermissionCommandHandler;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

public class CreatePermissionTest {
    private final PermissionRepository repository = new InMemoryPermissionRepository();

    private CreatePermissionCommandHandler commandHandler() {
        return new CreatePermissionCommandHandler(repository);
    }

    Permission permission = new Permission(
            "permission-1",
            "title",
            Category.MANAGE_SCHEDULE,
            "description"
    );

    @BeforeEach
    void setUp() {
        this.repository.clear();
        this.repository.save(permission);
    }

    @Nested
    class Scenario_HappyPath {
        @Test
        void shouldCreatePermission() {
            var useCase = commandHandler();

            var command = new CreatePermissionCommand(
                    "title",
                    Category.CREATE_EVENT,
                    "description"
            );

            var idResponse = useCase.handle(command);

            var permissionQuery = repository.findById(idResponse.getId());
            Assertions.assertNotNull(permissionQuery);

            var permission = permissionQuery.get();

            Assertions.assertEquals(permission.getTitle(), command.getTitle());
            Assertions.assertEquals(permission.getCategory(), command.getCategory());
            Assertions.assertEquals(permission.getDescription(), command.getDescription());
        }

        @Test
        void shouldCreatePermission_withEmptyDescription() {
            var useCase = commandHandler();

            var command = new CreatePermissionCommand(
                    "title",
                    Category.CREATE_EVENT,
                    null
            );

            var idResponse = useCase.handle(command);

            var permissionQuery = repository.findById(idResponse.getId());
            Assertions.assertNotNull(permissionQuery);

            var permission = permissionQuery.get();

            Assertions.assertEquals(permission.getTitle(), command.getTitle());
            Assertions.assertEquals(permission.getCategory(), command.getCategory());
            Assertions.assertNull(permission.getDescription());
        }

        @Test
        void shouldCreatePermission_whenDescriptionIsEmpty() {
            var useCase = commandHandler();

            var command = new CreatePermissionCommand(
                    "title",
                    Category.CREATE_EVENT,
                    null
            );

            var idResponse = useCase.handle(command);

            var permissionQuery = repository.findById(idResponse.getId());
            Assertions.assertNotNull(permissionQuery);

            var permission = permissionQuery.get();

            Assertions.assertEquals(permission.getTitle(), command.getTitle());
            Assertions.assertEquals(permission.getCategory(), command.getCategory());
            Assertions.assertNull(permission.getDescription());
        }
    }


    @Nested
    class Scenario_PermissionAlreadyExist {
        @Test
        void shouldThrow_PermissionAlreadyExist() {
            var useCase = commandHandler();

            var command = new CreatePermissionCommand(
                    "title",
                    Category.MANAGE_SCHEDULE,
                    null
            );

            var exception = Assertions.assertThrows(
                    PermissionAlreadyExistsException.class,
                    () -> useCase.handle(command)
            );

            Assertions.assertEquals("Permission with category " + command.getCategory() + " already exists" , exception.getMessage());
        }
    }
}
