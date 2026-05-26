package com.example.comot.organisation.application.ports;

import com.example.comot.core.application.ports.BaseRepository;
import com.example.comot.organisation.domaine.model.Organisation;

import java.util.Optional;

public interface OrganisationRepository extends BaseRepository<Organisation> {
    Optional<Organisation> findByUserId(String userId);
    Optional<Organisation> findByName(String name);
}
