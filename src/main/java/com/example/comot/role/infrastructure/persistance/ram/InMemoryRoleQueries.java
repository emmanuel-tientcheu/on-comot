package com.example.comot.role.infrastructure.persistance.ram;

import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.domaine.viewModel.RoleViewModel;

public class InMemoryRoleQueries extends InMemoryBaseRepository<Role> implements RoleQueries {
    @Override
    public RoleViewModel findRoleById(String id) {
        var role = this.findById(id).get();

        var permissions = role.getPermissions()
                .stream()
                .map(
                        rolePermission -> new RoleViewModel.RolePermission(
                                rolePermission.getId(),
                                rolePermission.getPermission().getTitle(),
                                rolePermission.getPermissionId(),
                                rolePermission.getCategory()
                        )
                ).toList();

        return new RoleViewModel(
                role.getId(),
                role.getTitle(),
                role.getDescription(),
                role.isDefault(),
                permissions
        );
    }
}
