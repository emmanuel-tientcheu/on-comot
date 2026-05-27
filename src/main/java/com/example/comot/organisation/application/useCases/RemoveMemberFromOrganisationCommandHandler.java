package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;

public class RemoveMemberFromOrganisationCommandHandler implements Command.Handler<RemoveMemberFromOrganisationCommand, Void> {

    private final OrganisationRepository organisationRepository;

    public RemoveMemberFromOrganisationCommandHandler(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Void handle(RemoveMemberFromOrganisationCommand command) {
        var organisation = organisationRepository.findById(command.getOrganisationId())
                .orElseThrow(() -> new NotFoundException("Organisation", command.getOrganisationId()));

        if (!organisation.hasMember(command.getUserId())) {
            throw new BadRequestException("User is not a member of this organisation");
        }

        if (organisation.getUserId().equals(command.getUserId())) {
            throw new BadRequestException("Cannot remove the organisation owner");
        }

        organisation.removeMember(command.getUserId());

        organisationRepository.save(organisation);

        return null;
    }
}
