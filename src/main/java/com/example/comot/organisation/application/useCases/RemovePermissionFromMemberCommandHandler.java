package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.permission.application.ports.PermissionRepository;

public class RemovePermissionFromMemberCommandHandler  implements Command.Handler<RemovePermissionFromMemberCommand, Void> {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public RemovePermissionFromMemberCommandHandler(
            OrganisationRepository organisationRepository,
            UserRepository userRepository,
            PermissionRepository permissionRepository
    ) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }
    @Override
    public Void handle(RemovePermissionFromMemberCommand command) {
        var organisation = organisationRepository.findById(command.getOrganisationId())
                .orElseThrow(() -> new NotFoundException("Organisation", command.getOrganisationId()));

        var user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException("User", command.getUserId()));

        var permission = permissionRepository.findById(command.getPermissionId())
                .orElseThrow(() -> new NotFoundException("Permission", command.getPermissionId()));

        if (!organisation.hasMember(command.getUserId())) {
            throw new BadRequestException("User is not a member of this organisation");
        }

        if (!organisation.hasPermission(command.getUserId(), command.getPermissionId())) {
            throw new BadRequestException("User does not have this permission");
        }

        organisation.removePermission(command.getUserId(), command.getPermissionId());

        organisationRepository.save(organisation);

        return null;
    }
}
