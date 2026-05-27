package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;

public class RemovePermissionFromMemberCommand implements Command<Void> {
    private String organisationId;
    private String userId;
    private String permissionId;

    public RemovePermissionFromMemberCommand() {}

    public RemovePermissionFromMemberCommand(String organisationId, String userId, String permissionId) {
        this.organisationId = organisationId;
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPermissionId() {
        return permissionId;
    }
}
