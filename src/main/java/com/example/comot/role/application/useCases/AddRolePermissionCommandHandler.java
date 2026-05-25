package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;

public class AddRolePermissionCommandHandler implements Command.Handler<AddRolePermissionCommand, Void> {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AddRolePermissionCommandHandler(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    )
    {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Void handle(AddRolePermissionCommand command) {
        var role = this.roleRepository.findById(command.getRoleId()).orElseThrow(
                () -> new NotFoundException("Role", command.getRoleId())
        );

        var permission = this.permissionRepository.findById(command.getPermissionId()).orElseThrow(
                () -> new NotFoundException("Permission", command.getPermissionId())
        );

        role.addPermission(permission.getId(), permission.getCategory());

        roleRepository.save(role);

        return null;
    }
}
