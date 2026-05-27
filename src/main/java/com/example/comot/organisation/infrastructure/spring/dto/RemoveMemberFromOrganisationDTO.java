package com.example.comot.organisation.infrastructure.spring.dto;

public class RemoveMemberFromOrganisationDTO {
    private String organisationId;
    private String userId;

    public RemoveMemberFromOrganisationDTO() {}

    public RemoveMemberFromOrganisationDTO(String organisationId, String userId) {
        this.organisationId = organisationId;
        this.userId = userId;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }

}
