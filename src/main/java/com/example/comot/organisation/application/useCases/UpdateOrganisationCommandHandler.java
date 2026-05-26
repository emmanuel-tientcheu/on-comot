package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.organisation.application.ports.OrganisationRepository;

public class UpdateOrganisationCommandHandler implements Command.Handler<UpdateOrganisationCommand, Void> {

    private final OrganisationRepository organisationRepository;

    public UpdateOrganisationCommandHandler(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Void handle(UpdateOrganisationCommand command) {
        var organisation = this.organisationRepository.findById(command.getOrganisationId()).orElseThrow(
                () -> new NotFoundException("Organisation", command.getOrganisationId())
        );

        organisation.setName(command.getName());
        organisation.setDescription(command.getDescription());

        this.organisationRepository.save(organisation);

        return null;
    }
}
