package com.example.comot.organisation;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommand;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.persistance.ram.InMemoryPermissionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateOrganisationTest {
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();
    private final PermissionRepository permissionRepository = new InMemoryPermissionRepository();

    User user = new User(
            "user-1",
            "emmanuel",
            "keou",
            "emmanuelkeou@gmail.com",
            "password"
    );

    Permission permission = new Permission(
            "perm-1",
            "title",
            Category.CREATE_EVENT,
            "description"
    );

    private CreateOrganisationCommandHandler commandHandler() {
        return new CreateOrganisationCommandHandler(organisationRepository, userRepository, permissionRepository);
    }

    @BeforeEach
    void setUp() {
        userRepository.clear();
        permissionRepository.clear();
        userRepository.save(user);
        permissionRepository.save(permission);
    }

    @Test
    void shouldCreateOrganisation() {
        var command = new CreateOrganisationCommand(
                "name",
                "description",
                true,
                user.getId()
        );

        var useCase = commandHandler();

        var idResponse = useCase.handle(command);

        var orgQuery = organisationRepository.findById(idResponse.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var organisation = orgQuery.get();

        Assertions.assertEquals(organisation.getName(), command.getName());
        Assertions.assertEquals(organisation.getDescription(), command.getDescription());
        Assertions.assertTrue(organisation.getActive());
        Assertions.assertEquals(organisation.getUserId(), command.getUserId());

        var permission = permissionRepository.findByCategory(Category.CREATE_EVENT).get();

        Assertions.assertTrue(organisation.hasMember(user.getId()));
        Assertions.assertTrue(organisation.hasPermission(user.getId(), permission.getId()));
    }

    @Test
    void whenUserHaveAlreadyOrganisation_shouldThrow() {
        var organisation = new Organisation(
                "org-1",
                "first org",
                "description",
                true,
                user.getId()
        );

        this.organisationRepository.save(organisation);

        var command = new CreateOrganisationCommand(
                "name",
                "description",
                true,
                user.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("User already has an organisation", exception.getMessage());

    }

    @Test
    void whenNameAreAlreadyUse_shouldThrow() {
        var organisation = new Organisation(
                "org-1",
                "name",
                "description",
                true,
                user.getId()
        );

        this.organisationRepository.save(organisation);

        var command = new CreateOrganisationCommand(
                "name",
                "description",
                true,
                user.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("Organisation with this name already exists", exception.getMessage());

    }
}
