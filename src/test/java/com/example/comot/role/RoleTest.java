package com.example.comot.role;

import com.example.comot.permission.domaine.model.Category;
import com.example.comot.role.domaine.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class RoleTest {

    @Nested
    class AddRole {
        @Test
        void shouldAddPermission() {
            var role = new Role(
                    "role-1",
                    "title",
                    "description",
                    true
            );

            role.addPermission(
                    "permission_" + Category.CREATE_EVENT,
                    Category.CREATE_EVENT
            );

            Assertions.assertTrue(role.hasPermission(Category.CREATE_EVENT));
        }

        @Test
        void whenPermissionAlReadyExist_shouldThrow() {
            var role = new Role(
                    "role-1",
                    "title",
                    "description",
                    true
            );

            role.addPermission(
                    "permission_" + Category.CREATE_EVENT,
                    Category.CREATE_EVENT
            );

            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> role.addPermission(
                            "permission_" + Category.CREATE_EVENT,
                            Category.CREATE_EVENT
                    )
            );
        }
    }

    @Nested
    class RemovePermission {
        @Test
        void shouldRemovePermission() {
            var role = new Role(
                    "role-1",
                    "title",
                    "description",
                    true
            );

            role.addPermission(
                    "permission_" + Category.CREATE_EVENT,
                    Category.CREATE_EVENT
            );
            Assertions.assertTrue(role.hasPermission(Category.CREATE_EVENT));

            role.removePermission(
                    "permission_" + Category.CREATE_EVENT
            );
            Assertions.assertFalse(role.hasPermission(Category.CREATE_EVENT));

        }

        @Test
        void whenPermissionNotExist_shouldThrow() {
            var role = new Role(
                    "role-1",
                    "title",
                    "description",
                    true
            );
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> role.removePermission(
                            "permission_" + Category.CREATE_EVENT
                    )
            );
        }
    }
}
