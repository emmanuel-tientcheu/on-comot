package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.domaine.viewModel.RoleViewModel;

public class GetRoleCommandHandler implements Command.Handler<GetRoleCommand, RoleViewModel> {
    public final RoleQueries roleQueries;

    public GetRoleCommandHandler(RoleQueries roleQueries) {
        this.roleQueries = roleQueries;
    }

    @Override
    public RoleViewModel handle(GetRoleCommand command) {
        return roleQueries.findRoleById(command.getRoleId());
    }
}
