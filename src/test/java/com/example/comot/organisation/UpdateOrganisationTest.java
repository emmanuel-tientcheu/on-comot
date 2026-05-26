package com.example.comot.organisation;

import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.UpdateOrganisationCommand;
import com.example.comot.organisation.application.useCases.UpdateOrganisationCommandHandler;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpdateOrganisationTest {
    private final OrganisationRepository organisationRepository = new InMemoryOrganisationRepository();

    private UpdateOrganisationCommandHandler commandHandler() {
        return new UpdateOrganisationCommandHandler(organisationRepository);
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
    void shouldUpdateRole() {
        var command = new UpdateOrganisationCommand(
                organisation.getId(),
                "new name",
                "new description"
        );

        var useCase = commandHandler();

        useCase.handle(command);

        var org = this.organisationRepository.findById(organisation.getId()).get();

        Assertions.assertEquals(org.getName(), command.getName());
        Assertions.assertEquals(org.getDescription(), command.getDescription());
    }
}
