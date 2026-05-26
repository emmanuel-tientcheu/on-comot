package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.organisation.application.ports.OrganisationRepository;

public class DeleteOrganisationCommandHandler implements Command.Handler<DeleteOrganisationCommand, Void> {

    private final OrganisationRepository organisationRepository;

    public DeleteOrganisationCommandHandler(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Void handle(DeleteOrganisationCommand command) {

        var organisation = this.organisationRepository.findById(command.getOrgId()).orElseThrow(
                () -> new NotFoundException("Organisation", command.getOrgId())
        );

        this.organisationRepository.delete(organisation);
        return null;
    }
}
