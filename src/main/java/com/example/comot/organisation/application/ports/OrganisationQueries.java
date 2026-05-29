package com.example.comot.organisation.application.ports;

import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;

import java.util.List;
import java.util.Optional;

public interface OrganisationQueries {
    public OrganisationViewModel findById(String id);
    public Optional<MemberOrganisationViewModel> getMember(String orgId, String userId);
    public OrganisationMembersViewModel getMembers(String orgId);
}
