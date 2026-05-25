package com.example.comot.role.infrastructure.spring.dto;

public class AddRolePermissionDTO {
    private String roleId;
    private String permissionId;

    public AddRolePermissionDTO() {}

    public AddRolePermissionDTO(String roleId, String permissionId) {
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
