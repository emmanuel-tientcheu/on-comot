package com.example.comot.permission.infrastructure.spring.dto;

import com.example.comot.permission.domaine.model.Category;

public class CreatePermissionDTO {
    private String title;
    private String category;
    private String description;

    public CreatePermissionDTO() {}

    public CreatePermissionDTO(String title, String category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return Category.formString(category);
    }

    public String getDescription() {
        return description;
    }
}
