package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;

public class DeleteOrganisationCommand implements Command<Void> {
    private String orgId;

    public DeleteOrganisationCommand() {}

    public DeleteOrganisationCommand(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() { return orgId; }
}
