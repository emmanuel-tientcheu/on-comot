package com.example.comot.permission;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.application.useCases.GetPermissionsHandler;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

public class GetPermissionsTest {
    private final PermissionRepository repository = new InMemoryPermissionRepository();

    @Nested
    class Scenario_ShouldHavePermission {
        @BeforeEach
        void setUp() {
            for (Category category: Category.values()) {
                var permission = new Permission(
                        "permission_" + category.toString(),
                        "title",
                        category,
                        "description"
                );

                repository.save(permission);
            }
        }

        @Test
        void shouldGetCategories() {
            var useCase = new GetPermissionsHandler(repository);

            var result = useCase.handle();

            Assertions.assertEquals(result.size(), Category.values().length - 1);
        }
    }

    @Nested
    class Scenario_ShouldDoesNotHavePermission {

        @Test
        void shouldGetCategories() {
            var useCase = new GetPermissionsHandler(repository);

            var result = useCase.handle();

            Assertions.assertEquals(result.size(), 0);
        }
    }
}
