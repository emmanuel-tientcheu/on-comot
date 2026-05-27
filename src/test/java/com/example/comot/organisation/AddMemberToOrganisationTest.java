package com.example.comot.organisation;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.AddMemberToOrganisationCommand;
import com.example.comot.organisation.application.useCases.AddMemberToOrganisationCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddMemberToOrganisationTest {
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();

    private User owner;
    private User memberToAdd;
    private Organisation organisation;

    private AddMemberToOrganisationCommandHandler commandHandler() {
        return new AddMemberToOrganisationCommandHandler(organisationRepository, userRepository);
    }

    @BeforeEach
    void setUp() {
        userRepository.clear();
        organisationRepository.clear();

        owner = new User(
                "owner-1",
                "Emmanuel",
                "Keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        memberToAdd = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        organisation = new Organisation(
                "org-1",
                "My Organisation",
                "Description",
                true,
                owner.getId()
        );

        userRepository.save(owner);
        userRepository.save(memberToAdd);
        organisationRepository.save(organisation);
    }

    @Test
    void shouldAddMemberToOrganisation() {
        var command = new AddMemberToOrganisationCommand(
                organisation.getId(),
                memberToAdd.getId()
        );

        var useCase = commandHandler();

        useCase.handle(command);

        var orgQuery = organisationRepository.findById(organisation.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var updatedOrganisation = orgQuery.get();
        Assertions.assertTrue(updatedOrganisation.hasMember(memberToAdd.getId()));
    }

    @Test
    void whenMemberAlreadyInOrganisation_shouldThrow() {
        organisation.addMember(memberToAdd.getId());
        organisationRepository.save(organisation);

        var command = new AddMemberToOrganisationCommand(
                organisation.getId(),
                memberToAdd.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("User is already a member of this organisation", exception.getMessage());
    }

    @Test
    void whenOrganisationNotFound_shouldThrow() {
        var command = new AddMemberToOrganisationCommand(
                "non-existent-org",
                memberToAdd.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("Organisation with the key non-existent-org was not found", exception.getMessage());
    }

    @Test
    void whenUserNotFound_shouldThrow() {
        var command = new AddMemberToOrganisationCommand(
                organisation.getId(),
                "non-existent-user"
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("User with the key non-existent-user was not found", exception.getMessage());
    }

    @Test
    void whenOwnerTryToAddHimselfAsMember_shouldThrow() {
        var command = new AddMemberToOrganisationCommand(
                organisation.getId(),
                owner.getId()
        );

        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("The organisation owner is automatically a member", exception.getMessage());
    }

}
