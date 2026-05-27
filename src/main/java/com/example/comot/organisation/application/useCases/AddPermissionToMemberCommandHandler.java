package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.permission.application.ports.PermissionRepository;

public class AddPermissionToMemberCommandHandler implements Command.Handler<AddPermissionToMemberCommand, Void> {

    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public AddPermissionToMemberCommandHandler(
            OrganisationRepository organisationRepository,
            UserRepository userRepository,
            PermissionRepository permissionRepository
    ) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Void handle(AddPermissionToMemberCommand command) {
        var organisation = organisationRepository.findById(command.getOrganisationId())
                .orElseThrow(() -> new NotFoundException("Organisation", command.getOrganisationId()));

        var user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException("User", command.getUserId()));

        var permission = permissionRepository.findById(command.getPermissionId())
                .orElseThrow(() -> new NotFoundException("Permission", command.getPermissionId()));

        if (!organisation.hasMember(command.getUserId())) {
            throw new BadRequestException("User is not a member of this organisation");
        }

        organisation.addPermission(command.getUserId(), command.getPermissionId(), command.getCategory());

        organisationRepository.save(organisation);

        return null;
    }
}
