package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;

public class GetOrganisationCommandHandler implements Command.Handler<GetOrganisationCommand, OrganisationViewModel> {
    private final OrganisationQueries organisationQueries;

    public GetOrganisationCommandHandler(OrganisationQueries organisationQueries) {
        this.organisationQueries = organisationQueries;
    }

    @Override
    public OrganisationViewModel handle(GetOrganisationCommand command) {
        return this.organisationQueries.findById(command.getId());
    }
}
