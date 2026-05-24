package com.example.comot.permission.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.domaine.model.Category;

public class CreatePermissionCommand implements Command<IdResponse> {
    private String title;
    private Category category;
    private String description;

    public CreatePermissionCommand() {}

    public CreatePermissionCommand(String title, Category category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
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
