package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;

public class GetMemberCommandHandler implements Command.Handler<GetMemberCommand, MemberOrganisationViewModel> {
    private final OrganisationQueries organisationQueries;

    public GetMemberCommandHandler(OrganisationQueries organisationQueries) {
        this.organisationQueries = organisationQueries;
    }

    @Override
    public MemberOrganisationViewModel handle(GetMemberCommand command) {
        return organisationQueries.getMember(command.getOrganisationId(), command.getUserId())
                .orElseThrow(() -> new NotFoundException(
                        "Member not found for organisation " + command.getOrganisationId() + " and user ",
                        command.getUserId()
                ));
    }
}
