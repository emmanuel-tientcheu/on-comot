package com.example.comot.role.infrastructure.persistance.sql;

import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.domaine.viewModel.RoleViewModel;
import jakarta.persistence.EntityManager;

public class SQLRoleQueries implements RoleQueries {
    private final EntityManager entityManager;

    public SQLRoleQueries(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RoleViewModel findRoleById(String id) {
        var query = this.entityManager.createQuery(
                "SELECT DISTINCT r FROM Role r " +
                        "JOIN FETCH r.permissions p " +
                        "JOIN FETCH p.permission " +
                "WHERE r.id = :id",
                Role.class
        );

        query.setParameter("id", id);

        var result = query.getSingleResult();

        var permissions = result.getPermissions()
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
                result.getId(),
                result.getTitle(),
                result.getDescription(),
                result.isDefault(),
                permissions
        );
    }
}
