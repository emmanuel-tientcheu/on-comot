package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.role.domaine.viewModel.RoleViewModel;

public class GetRoleCommand implements Command<RoleViewModel> {
    private String roleId;

    public GetRoleCommand() {}

    public GetRoleCommand(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() { return roleId; }
}
