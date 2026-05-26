package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;

import java.awt.font.TextHitInfo;

public class UpdateOrganisationCommand implements Command<Void> {
    private String organisationId;
    private String name;
    private String description;

    public UpdateOrganisationCommand() {}

    public UpdateOrganisationCommand(String organisationId, String name, String description) {
        this.organisationId = organisationId;
        this.name = name;
        this.description = description;
    }

    public String getOrganisationId() { return organisationId; }

    public String getName() { return name; }

    public String getDescription() { return description; }


}
