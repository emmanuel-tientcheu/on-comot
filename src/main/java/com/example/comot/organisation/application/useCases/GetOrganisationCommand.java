package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;

public class GetOrganisationCommand implements Command<OrganisationViewModel> {
    private String id;

    public GetOrganisationCommand() {}

    public GetOrganisationCommand(String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
