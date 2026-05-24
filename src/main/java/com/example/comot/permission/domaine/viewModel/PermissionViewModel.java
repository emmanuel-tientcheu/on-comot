package com.example.comot.permission.domaine.viewModel;

import com.example.comot.permission.domaine.model.Category;

public class PermissionViewModel {
    private String id;
    private String title;
    private Category category;
    private String description;

    public PermissionViewModel() {}

    public PermissionViewModel(String id, String title, Category category, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
