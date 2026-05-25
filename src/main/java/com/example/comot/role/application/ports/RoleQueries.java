package com.example.comot.role.application.ports;

import com.example.comot.role.domaine.viewModel.RoleViewModel;

public interface RoleQueries {
    public RoleViewModel findRoleById(String id);
}
