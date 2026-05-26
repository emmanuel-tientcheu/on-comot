package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;

public class CreateOrganisationCommand implements Command<IdResponse> {

    private String name;
    private String description;
    private Boolean active;
    private String userId;

    public CreateOrganisationCommand() {}

    public CreateOrganisationCommand(String name, String description, Boolean active, String userId) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.userId = userId;
    }

    public String getUserId() { return userId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getActive(){ return active; }
}
