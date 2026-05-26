package com.example.comot.organisation.infrastructure.spring.dto;

public class CreateOrganisationDTO {
    private String name;
    private String description;
    private Boolean active;
    private String userId;

    public CreateOrganisationDTO() {}

    public CreateOrganisationDTO(String name, String description, Boolean active, String userId) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.userId = userId;
    }

    public String getUserId() { return userId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getActive(){ return active; }
}
