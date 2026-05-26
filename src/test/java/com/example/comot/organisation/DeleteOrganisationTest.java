package com.example.comot.organisation;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.DeleteOrganisationCommand;
import com.example.comot.organisation.application.useCases.DeleteOrganisationCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteOrganisationTest {
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();

    private DeleteOrganisationCommandHandler commandHandler() {
        return new DeleteOrganisationCommandHandler(organisationRepository);
    }

    Organisation organisation;

    @BeforeEach
    void setUp() {
        organisation = new Organisation(
                "org-1",
                "first org",
                "description",
                true,
                "user-id"
        );

        this.organisationRepository.save(organisation);
    }

    @Test
    void shouldDeleteOrganisation() {
        var command = new DeleteOrganisationCommand(organisation.getId());
        var useCase = commandHandler();

        useCase.handle(command);

        var organisation = this.organisationRepository.findById(this.organisation.getId());

        Assertions.assertFalse(organisation.isPresent());
        Assertions.assertTrue(organisation.isEmpty());
    }

    @Test
    void whenOrganisationDoesNotExist_shouldThrow() {
        var command = new DeleteOrganisationCommand("garbage");
        var useCase = commandHandler();


      Assertions.assertThrows(
              NotFoundException.class,
              () -> useCase.handle(command)
      );
    }
}
