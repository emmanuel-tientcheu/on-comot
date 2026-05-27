package com.example.comot.organisation;

import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrganisationTest {

    Organisation org = new Organisation(
            "org-1",
            "org-name",
            "org-description",
            true,
            "user-1"
    );
    User user = new User(
            "user-1",
            "firstname",
            "lastname",
            "user@gmail.com",
            "password"
    );

    @Nested
    class AddMember {
        @Test
        void shouldAddMember() {
            org.addMember(user.getId());
            Assertions.assertTrue(org.hasMember(user.getId()));
        }

        @Test
        void whenUserIdAlreadyInOrg_shouldThrow() {
            org.addMember(user.getId());

            var exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    ()-> org.addMember(user.getId())
            );

            Assertions.assertEquals(
                    "This member is already in the organisation",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class RemoveMember {
        @Test
        void shouldRemoveMember() {
            org.addMember(user.getId());
            Assertions.assertTrue(org.hasMember(user.getId()));

            org.removeMember(user.getId());
            Assertions.assertFalse(org.hasMember(user.getId()));
        }

        @Test
        void whenUserIsNotInTheTeam_shouldThrow() {
            org.addMember(user.getId());

            org.removeMember(user.getId());
            Assertions.assertFalse(org.hasMember(user.getId()));

            var exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    ()-> org.removeMember(user.getId())
            );

            Assertions.assertEquals(
                    "This member is not in the organisation",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class AddPermission {
        Permission permission = new Permission(
                "permission-id",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        @Test
        void shouldAddPermission() {
            org.addMember(user.getId());

            org.addPermission(user.getId(), permission.getId(), permission.getCategory());

            Assertions.assertTrue(org.hasPermission(user.getId(), permission.getId()));
        }

        @Test
        void whenUserIsNotAMember_shouldThrow() {
            var exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> org.addPermission("garbage", permission.getId(), permission.getCategory())

            );

            Assertions.assertEquals(
                    "This user is not in the organisation",
                    exception.getMessage()
            );
        }

        @Test
        void whenPermissionAlReadyExist_shouldThrow() {
            org.addMember(user.getId());

            org.addPermission(user.getId(), permission.getId(), permission.getCategory());

            var exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> org.addPermission(user.getId(), permission.getId(), permission.getCategory())

            );

            Assertions.assertEquals(
                    "This permission is already assign to this member",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class RemovePermission {
        Permission permission = new Permission(
                "permission-id",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        @Test
        void shouldRemovePermission() {
            org.addMember(user.getId());

            org.addPermission(user.getId(), permission.getId(), permission.getCategory());

            org.removePermission(user.getId(), permission.getId());

            Assertions.assertFalse(org.hasPermission(user.getId(), permission.getId()));
        }

        @Test
        void whenPermissionNotExist_shouldThrow() {
            org.addMember(user.getId());

            var exception = Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () ->  org.removePermission(user.getId(), "garbage")

            );

            Assertions.assertEquals(
                    "This user have not this permission",
                    exception.getMessage()
            );
        }
    }
}
