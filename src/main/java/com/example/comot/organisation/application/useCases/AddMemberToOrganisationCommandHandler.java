package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;

public class AddMemberToOrganisationCommandHandler implements Command.Handler<AddMemberToOrganisationCommand, Void> {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;

    public AddMemberToOrganisationCommandHandler(
            OrganisationRepository organisationRepository,
            UserRepository userRepository
    ) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Void handle(AddMemberToOrganisationCommand command) {
        var organisation = organisationRepository.findById(command.getOrganisationId())
                .orElseThrow(() -> new NotFoundException("Organisation", command.getOrganisationId()));

        var user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException("User", command.getUserId()));

        if (organisation.hasMember(command.getUserId())) {
            throw new BadRequestException("User is already a member of this organisation");
        }

        if (organisation.getUserId().equals(command.getUserId())) {
            throw new BadRequestException("The organisation owner is automatically a member");
        }

        organisation.addMember(command.getUserId());

        organisationRepository.save(organisation);

        return null;
    }
}
