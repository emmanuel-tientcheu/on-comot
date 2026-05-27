package com.example.comot.organisation.infrastructure.spring.dto;

public class AddMemberToOrganisationDTO {
    private String organisationId;
    private String userId;

    public AddMemberToOrganisationDTO() {}

    public AddMemberToOrganisationDTO(String organisationId, String userId) {
        this.organisationId = organisationId;
        this.userId = userId;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }
}
