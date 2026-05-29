package com.example.comot.organisation.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationsViewModel;

public class GetUserOrganisationsCommand implements Command<MemberOrganisationsViewModel> {
    private String userId;

    public GetUserOrganisationsCommand() {}

    public GetUserOrganisationsCommand(String userId) { this.userId = userId; }

    public String getUserId() { return userId; }
}
