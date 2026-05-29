package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;

public class GetOrganisationMembersCommandHandler implements Command.Handler<GetOrganisationMembersCommand, OrganisationMembersViewModel>{
    private final OrganisationQueries organisationQueries;

    public GetOrganisationMembersCommandHandler(OrganisationQueries organisationQueries) {
        this.organisationQueries = organisationQueries;
    }

    @Override
    public OrganisationMembersViewModel handle(GetOrganisationMembersCommand command) {
        return organisationQueries.getMembers(command.getOrganisationId());
    }
}
