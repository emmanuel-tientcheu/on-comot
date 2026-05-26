package com.example.comot.organisation.infrastructure.spring.dto;

public class GetOrganisationDTO {
    private String id;

    public GetOrganisationDTO() {}

    public GetOrganisationDTO(String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
