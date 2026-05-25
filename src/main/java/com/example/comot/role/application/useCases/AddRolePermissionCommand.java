package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.role.domaine.viewModel.RoleViewModel;

public class AddRolePermissionCommand implements Command<Void> {
    private String roleId;
    private String permissionId;

    public AddRolePermissionCommand() {}

    public AddRolePermissionCommand(String roleId, String permissionId) {
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
