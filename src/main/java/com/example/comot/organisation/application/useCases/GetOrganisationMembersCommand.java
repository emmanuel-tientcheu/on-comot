package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;

public class GetOrganisationMembersCommand implements Command<OrganisationMembersViewModel> {
    private String organisationId;

    public GetOrganisationMembersCommand() {}

    public GetOrganisationMembersCommand(String organisationId) { this.organisationId = organisationId; }

    public String getOrganisationId() { return organisationId; }
}
