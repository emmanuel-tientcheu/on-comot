package com.example.comot.organisation.infrastructure.spring.dto;

import com.example.comot.permission.domaine.model.Category;

public class AddPermissionToMemberDTO {
    private String organisationId;
    private String userId;
    private String permissionId;
    private String category;

    public AddPermissionToMemberDTO() {}

    public AddPermissionToMemberDTO(String organisationId, String userId, String permissionId, String category) {
        this.organisationId = organisationId;
        this.userId = userId;
        this.permissionId = permissionId;
        this.category = category;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }

    public String getPermissionId() { return permissionId; }

    public Category getCategory() { return Category.formString(category); }


}
