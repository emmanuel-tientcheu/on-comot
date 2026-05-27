package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.domaine.model.Category;

public class AddPermissionToMemberCommand implements Command<Void> {
    private String organisationId;
    private String userId;
    private String permissionId;
    private Category category;

    public AddPermissionToMemberCommand() {}

    public AddPermissionToMemberCommand(String organisationId, String userId, String permissionId, Category category) {
        this.organisationId = organisationId;
        this.userId = userId;
        this.permissionId = permissionId;
        this.category = category;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }

    public String getPermissionId() { return permissionId; }

    public Category getCategory() { return category; }
}
