package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;

public class AddMemberToOrganisationCommand implements Command<Void> {
    private String organisationId;
    private String userId;

    public AddMemberToOrganisationCommand() {}

    public AddMemberToOrganisationCommand(String organisationId, String userId) {
        this.organisationId = organisationId;
        this.userId = userId;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }
}
