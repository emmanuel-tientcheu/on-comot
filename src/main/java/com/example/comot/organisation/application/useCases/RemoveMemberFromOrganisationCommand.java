package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;

public class RemoveMemberFromOrganisationCommand implements Command<Void> {
    private String organisationId;
    private String userId;

    public RemoveMemberFromOrganisationCommand() {}

    public RemoveMemberFromOrganisationCommand(String organisationId, String userId) {
        this.organisationId = organisationId;
        this.userId = userId;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }
}
