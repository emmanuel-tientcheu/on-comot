package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.role.application.ports.RoleRepository;

public class RemoveRolePermissionCommandHandler implements Command.Handler<RemoveRolePermissionCommand, Void> {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RemoveRolePermissionCommandHandler(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Void handle(RemoveRolePermissionCommand command) {
        var role = this.roleRepository.findById(command.getRoleId()).orElseThrow(
                () -> new NotFoundException("Role", command.getRoleId())
        );

        var permission = this.permissionRepository.findById(command.getPermissionId()).orElseThrow(
                () -> new NotFoundException("Permission", command.getPermissionId())
        );

        role.removePermission(command.getPermissionId());

        this.roleRepository.save(role);

        return null;
    }
}
