package com.example.comot.organisation;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.RemovePermissionFromMemberCommand;
import com.example.comot.organisation.application.useCases.RemovePermissionFromMemberCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RemovePermissionFromMemberTest {

    private final UserRepository userRepository = new InMemoryUserRepository();
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();
    private final PermissionRepository permissionRepository = new InMemoryPermissionRepository();

    private User owner;
    private User member;
    private Organisation organisation;
    private Permission permission;

    private RemovePermissionFromMemberCommandHandler commandHandler() {
        return new RemovePermissionFromMemberCommandHandler(
                organisationRepository,
                userRepository,
                permissionRepository
        );
    }

    @BeforeEach
    void setUp() {
        userRepository.clear();
        organisationRepository.clear();
        permissionRepository.clear();

        owner = new User(
                "owner-1",
                "Emmanuel",
                "Keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        member = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        permission = new Permission(
                "perm-1",
                "View Sales",
                Category.VIEW_SALES,
                "Can view sales"
        );

        organisation = new Organisation(
                "org-1",
                "My Organisation",
                "Description",
                true,
                owner.getId()
        );

        userRepository.save(owner);
        userRepository.save(member);
        permissionRepository.save(permission);
        organisationRepository.save(organisation);

        organisation.addMember(member.getId());
        organisationRepository.save(organisation);

        organisation.addPermission(member.getId(), permission.getId(), permission.getCategory());
        organisationRepository.save(organisation);
    }


        @Test
        void shouldRemovePermissionFromMember() {
            var command = new RemovePermissionFromMemberCommand(
                    organisation.getId(),
                    member.getId(),
                    permission.getId()
            );
            var useCase = commandHandler();

            useCase.handle(command);

            var orgQuery = organisationRepository.findById(organisation.getId());
            Assertions.assertTrue(orgQuery.isPresent());

            var updatedOrganisation = orgQuery.get();
            Assertions.assertFalse(updatedOrganisation.hasPermission(member.getId(), permission.getId()));
        }

        @Test
        void whenUserDoesNotHavePermission_shouldThrow() {
            var permission2 = new Permission(
                    "perm-2",
                    "Manage Schedule",
                    Category.MANAGE_SCHEDULE,
                    "Can manage schedule"
            );
            permissionRepository.save(permission2);

            var command = new RemovePermissionFromMemberCommand(
                    organisation.getId(),
                    member.getId(),
                    permission2.getId()
            );
            var useCase = commandHandler();

            var exception = Assertions.assertThrows(
                    BadRequestException.class,
                    () -> useCase.handle(command)
            );

            Assertions.assertEquals(
                    "User does not have this permission",
                    exception.getMessage()
            );
        }

    @Test
    void whenMemberNotInOrganisation_shouldThrow() {
        var nonMember = new User(
                "non-member-1",
                "Jane",
                "Doe",
                "jane@test.com",
                "password"
        );
        userRepository.save(nonMember);

        var command = new RemovePermissionFromMemberCommand(
                organisation.getId(),
                nonMember.getId(),
                permission.getId()
        );
        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals(
                "User is not a member of this organisation",
                exception.getMessage()
        );
    }
    }
