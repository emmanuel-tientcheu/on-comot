package com.example.comot.role.infrastructure.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRoleDTO {
    private String title;
    private String description;
    @JsonProperty("isDefault")
    private boolean isDefault;

    public CreateRoleDTO() {}

    public CreateRoleDTO(String title, String description, Boolean isDefault) {
        this.title = title;
        this.description = description;
        this.isDefault = isDefault;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
