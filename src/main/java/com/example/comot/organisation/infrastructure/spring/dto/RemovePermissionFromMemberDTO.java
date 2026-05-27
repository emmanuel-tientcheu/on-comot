package com.example.comot.organisation.infrastructure.spring.dto;

public class RemovePermissionFromMemberDTO {

    private String organisationId;
    private String userId;
    private String permissionId;

    public RemovePermissionFromMemberDTO() {}

    public RemovePermissionFromMemberDTO(String organisationId, String userId, String permissionId) {
        this.organisationId = organisationId;
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPermissionId() {
        return permissionId;
    }

}
