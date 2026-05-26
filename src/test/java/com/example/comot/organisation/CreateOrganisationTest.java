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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateOrganisationTest {
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();

    User user = new User(
            "user-1",
            "emmanuel",
            "keou",
            "emmanuelkeou@gmail.com",
            "password"
    );

    private CreateOrganisationCommandHandler commandHandler() {
        return new CreateOrganisationCommandHandler(organisationRepository, userRepository);
    }

    @BeforeEach
    void setUp() {
        userRepository.clear();
        userRepository.save(user);
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
