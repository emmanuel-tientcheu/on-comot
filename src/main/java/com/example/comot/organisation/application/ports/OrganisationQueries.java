package com.example.comot.organisation.application.ports;

import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;

import java.util.Optional;

public interface OrganisationQueries {
    public OrganisationViewModel findById(String id);
}
