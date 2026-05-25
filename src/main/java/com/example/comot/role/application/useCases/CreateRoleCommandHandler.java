package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.domaine.model.Role;

import java.util.UUID;

public class CreateRoleCommandHandler implements Command.Handler<CreateRoleCommand, IdResponse> {
    private final RoleRepository repository;

    public CreateRoleCommandHandler(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public IdResponse handle(CreateRoleCommand command) {
        var role = new Role(
                UUID.randomUUID().toString(),
                command.getTitle(),
                command.getDescription(),
                command.isDefault()
        );

        this.repository.save(role);
        return new IdResponse(role.getId());
    }
}
