package com.example.comot.organisation.infrastructure.persistance.sql;

import com.example.comot.organisation.domaine.model.Organisation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SQLOrganisationAccessor extends CrudRepository<Organisation, String> {
    Optional<Organisation> findByUserId(String userId);
    Optional<Organisation> findByName(String name);
}
