package com.example.comot.permission.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.EmailUnavailableException;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.application.exceptions.PermissionAlreadyExistsException;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Permission;

import java.util.UUID;

public class CreatePermissionCommandHandler implements Command.Handler<CreatePermissionCommand, IdResponse> {
    private final PermissionRepository repository;

    public CreatePermissionCommandHandler(PermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public IdResponse handle(CreatePermissionCommand command) {
        this.repository.findByCategory(command.getCategory()).ifPresent(
                permission -> {
                    throw new PermissionAlreadyExistsException(command.getCategory());
                }
        );
        var permission = new Permission(
                UUID.randomUUID().toString(),
                command.getTitle(),
                command.getCategory(),
                command.getDescription()
        );

        this.repository.save(permission);
        return new IdResponse(
                permission.getId()
        );
    }
}
