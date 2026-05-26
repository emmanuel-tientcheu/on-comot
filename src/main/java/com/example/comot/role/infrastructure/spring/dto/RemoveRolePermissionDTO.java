package com.example.comot.role.infrastructure.spring.dto;

public class RemoveRolePermissionDTO {
    private String roleId;
    private String permissionId;

    public RemoveRolePermissionDTO() {}

    public RemoveRolePermissionDTO(String roleId, String permissionId) {
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
