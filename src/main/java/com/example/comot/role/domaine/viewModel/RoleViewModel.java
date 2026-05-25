package com.example.comot.role.domaine.viewModel;

import com.example.comot.permission.domaine.model.Category;
import com.example.comot.role.domaine.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;

public class RoleViewModel {
    private String id;
    private String title;
    private String description;
    @JsonProperty("default")
    private boolean isDefault;
    private List<RolePermission> permissions;

    public RoleViewModel() {}

    public RoleViewModel(String id, String title, String description, Boolean isDefault, List<RolePermission> permissions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDefault = isDefault;
        this.permissions = permissions;
    }

    public String getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public boolean isDefault() { return isDefault; }

    public List<RolePermission> getPermissions() { return  permissions; }

    public static class RolePermission {
        private String id;
        private String title;
        private String permissionId;
        private Category category;

        public RolePermission() {}

        public RolePermission(String id, String title, String permissionId, Category category) {
            this.id = id;
            this.title = title;
            this.permissionId = permissionId;
            this.category = category;
        }

        public String getId() { return id; }

        public String getTitle() { return title; }

        public String getPermissionId() { return permissionId; }

        public Category getCategory() { return category; }
    }
}
