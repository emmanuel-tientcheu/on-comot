package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationsViewModel;

public class GetUserOrganisationsCommandHandler  implements Command.Handler<GetUserOrganisationsCommand, MemberOrganisationsViewModel> {
    private final OrganisationQueries organisationQueries;

    public GetUserOrganisationsCommandHandler(OrganisationQueries organisationQueries) {
        this.organisationQueries = organisationQueries;
    }

    @Override
    public MemberOrganisationsViewModel handle(GetUserOrganisationsCommand command) {
        return organisationQueries.getOrganisationsUser(command.getUserId());
    }
}
