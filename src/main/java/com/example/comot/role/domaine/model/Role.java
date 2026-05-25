package com.example.comot.role.domaine.model;

import com.example.comot.core.domaine.model.BaseEntity;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Column
    private String title;
    @Column
    private String description;

    @Column(name = "is_default")
    private boolean isDefault;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<RolePermission> permissions;

    public Role() {}

    public Role(String id, String title, String description, Boolean isDefault) {
        super(id);
        this.title = title;
        this.description = description;
        this.isDefault = isDefault;
        this.permissions = new HashSet<RolePermission>();
    }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public boolean isDefault() { return isDefault; }

    public Set<RolePermission> getPermissions() { return  permissions; }

    public void addPermission(String permissionId, Category category) {
        if(this.permissions.stream().anyMatch(rolePermission -> rolePermission.getCategory().equals(category))) {
            throw new IllegalArgumentException("This permissions already exists in this role");
        }
        this.permissions.add(
                new RolePermission(
                        UUID.randomUUID().toString(),
                        permissionId,
                        this.getId(),
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

    @Entity
    @Table(name = "role_permissions")
    public static class RolePermission extends BaseEntity {
        @Column(name = "permission_id")
        private String permissionId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "permission_id", insertable = false, updatable = false)
        @MapsId("permissionId")
        private Permission permission;

        @Column(name = "role_id")
        private String roleId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role_id", insertable = false, updatable = false)
        @MapsId("roleId")
        private Role role;

        @Enumerated(EnumType.STRING)
        @Column(name = "category", nullable = false)
        private Category category;

        public RolePermission() {}

        public RolePermission(String id, String permissionId, String roleId, Category category) {
            super(id);
            this.permissionId = permissionId;
            this.roleId = roleId;
            this.category = category;
        }

        public String getPermissionId() { return permissionId; }

        public Category getCategory() { return category; }

        public Permission getPermission() { return  permission; }

        public String getRoleId() { return roleId; }
    }
}
