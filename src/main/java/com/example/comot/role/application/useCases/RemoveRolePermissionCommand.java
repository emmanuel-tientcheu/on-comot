package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;

public class RemoveRolePermissionCommand implements Command<Void> {
    private String roleId;
    private String permissionId;

    public RemoveRolePermissionCommand() {}

    public RemoveRolePermissionCommand(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }
}
