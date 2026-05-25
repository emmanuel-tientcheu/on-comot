package com.example.comot.role.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.role.domaine.model.Role;

import java.util.HashSet;

public class CreateRoleCommand implements Command<IdResponse> {
    private String title;
    private String description;
    private boolean isDefault;

    public CreateRoleCommand() {}

    public CreateRoleCommand(String title, String description, Boolean isDefault) {
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
