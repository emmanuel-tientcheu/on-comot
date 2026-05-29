package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;

public class GetMemberCommand implements Command<MemberOrganisationViewModel> {

    private String organisationId;
    private String userId;

    public GetMemberCommand() {}

    public GetMemberCommand(String organisationId, String userId) {
        this.organisationId = organisationId;
        this.userId = userId;
    }

    public String getOrganisationId() { return organisationId; }

    public String getUserId() { return userId; }
}
