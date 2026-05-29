package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.core.application.exceptions.BadRequestException;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;

public class CreateOrganisationCommandHandler implements Command.Handler<CreateOrganisationCommand, IdResponse> {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public CreateOrganisationCommandHandler(
            OrganisationRepository organisationRepository,
            UserRepository userRepository,
            PermissionRepository permissionRepository
    ) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public IdResponse handle(CreateOrganisationCommand command) {

        this.organisationRepository.findByName(command.getName()).ifPresent(
                organisation -> {
                    throw new BadRequestException("Organisation with this name already exists");
                }
        );

        this.organisationRepository.findByUserId(command.getUserId()).ifPresent(
                organisation -> {
                    throw new BadRequestException("User already has an organisation");
                }
        );


        var user = this.userRepository.findById(command.getUserId()).orElseThrow(
                () -> new NotFoundException("User", command.getUserId())
        );

        var organisation = new Organisation(
                command.getUserId(),
                command.getName(),
                command.getDescription(),
                command.getActive(),
                command.getUserId()
        );

        this.organisationRepository.save(organisation);

        organisation.addMember(user.getId());
        this.organisationRepository.save(organisation);

        var permission = this.permissionRepository.findByCategory(Category.CREATE_EVENT).orElseThrow(
                () -> new NotFoundException("Permission", Category.CREATE_EVENT.toString())
        );

        organisation.addPermission(user.getId(), permission.getId(), Category.CREATE_EVENT);
        this.organisationRepository.save(organisation);

        return new IdResponse(organisation.getId());
    }
}
