package com.example.comot.organisation;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.RemoveMemberFromOrganisationCommand;
import com.example.comot.organisation.application.useCases.RemoveMemberFromOrganisationCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RemoveMemberFromOrganisationTest {
    private final UserRepository userRepository = new InMemoryUserRepository();
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();

    private User owner;
    private User member;
    private Organisation organisation;

    private RemoveMemberFromOrganisationCommandHandler commandHandler() {
        return new RemoveMemberFromOrganisationCommandHandler(organisationRepository);
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
                "password")
        ;

        member = new User(
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
                true, owner.getId()
        );

        userRepository.save(owner);
        userRepository.save(member);
        organisationRepository.save(organisation);

        organisation.addMember(member.getId());
        organisationRepository.save(organisation);
    }

    @Test
    void shouldRemoveMemberFromOrganisation() {
        var command = new RemoveMemberFromOrganisationCommand(
                organisation.getId(),
                member.getId()
        );
        var useCase = commandHandler();

        useCase.handle(command);

        var orgQuery = organisationRepository.findById(organisation.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var updatedOrganisation = orgQuery.get();
        Assertions.assertFalse(updatedOrganisation.hasMember(member.getId()));
    }

    @Test
    void whenMemberNotFound_shouldThrow() {
        var command = new RemoveMemberFromOrganisationCommand(
                organisation.getId(),
                "non-existent-user"
        );
        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("User is not a member of this organisation", exception.getMessage());
    }

    @Test
    void whenOrganisationNotFound_shouldThrow() {
        var command = new RemoveMemberFromOrganisationCommand(
                "non-existent-org",
                member.getId()
        );
        var useCase = commandHandler();

        var exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("Organisation with the key non-existent-org was not found", exception.getMessage());
    }

//    @Test
//    void whenTryToRemoveOwner_shouldThrow() {
//        var command = new RemoveMemberFromOrganisationCommand(organisation.getId(), owner.getId());
//        var useCase = commandHandler();
//
//        var exception = Assertions.assertThrows(
//                BadRequestException.class,
//                () -> useCase.handle(command)
//        );
//
//        Assertions.assertEquals("Cannot remove the organisation owner", exception.getMessage());
//    }
}
