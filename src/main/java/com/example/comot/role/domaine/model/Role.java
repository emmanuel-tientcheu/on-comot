package com.example.comot.role.domaine.model;

import com.example.comot.core.domaine.model.BaseEntity;
import com.example.comot.permission.domaine.model.Category;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Role extends BaseEntity {
    private String title;
    private String description;
    private boolean isDefault;
    private Set<RolePermission> permissions;

    public Role() {}

    public Role(String id, String title, String description, Boolean isDefault) {
        super(id);
        this.title = title;
        this.description = description;
        this.isDefault = isDefault;
        this.permissions = new HashSet<RolePermission>();
    }

    public void addPermission(String permissionId, Category category) {
        if(this.permissions.stream().anyMatch(rolePermission -> rolePermission.getCategory().equals(category))) {
            throw new IllegalArgumentException("This permissions already exists in this role");
        }
        this.permissions.add(
                new RolePermission(
                        UUID.randomUUID().toString(),
                        permissionId,
                        category
                )
        );
    }

    public boolean hasPermission(Category category) {
        return this.permissions
                .stream()
                .anyMatch(permission -> permission.getCategory().equals(category));
    }

    public void removePermission(String permissionId) {
        if(this.permissions.stream().noneMatch(p -> p.getPermissionId().equals(permissionId))) {
            throw new IllegalArgumentException("This permission does not exist in this role");
        }

         this.permissions.removeIf(
                p -> p.getPermissionId().equals(permissionId)
        );
    }

    public static class RolePermission extends BaseEntity {
        private String permissionId;
        private Category category;

        public RolePermission() {}

        public RolePermission(String id, String permissionId, Category category) {
            super(id);
            this.permissionId = permissionId;
            this.category = category;
        }

        public String getPermissionId() {
            return permissionId;
        }

        public Category getCategory() {
            return category;
        }
    }
}
