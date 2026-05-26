package com.example.comot.organisation.infrastructure.spring.dto;

public class UpdateOrganisationDTO {
    private String organisationId;
    private String name;
    private String description;

    public UpdateOrganisationDTO() {}

    public UpdateOrganisationDTO(String organisationId, String name, String description) {
        this.organisationId = organisationId;
        this.name = name;
        this.description = description;
    }

    public String getOrganisationId() { return organisationId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

}
