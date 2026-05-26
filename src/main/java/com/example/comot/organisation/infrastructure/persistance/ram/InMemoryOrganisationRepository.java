package com.example.comot.organisation.infrastructure.persistance.ram;

import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;

import java.util.Optional;

public class InMemoryOrganisationRepository extends InMemoryBaseRepository<Organisation> implements OrganisationRepository {
    @Override
    public Optional<Organisation> findByUserId(String userId) {
        return this.entities
                .values()
                .stream()
                .filter(
                        organisation -> organisation.getUserId().equals(userId)
                ).findFirst();
    }

    @Override
    public Optional<Organisation> findByName(String name) {
        return this.entities
                .values()
                .stream()
                .filter(
                        organisation -> organisation.getName().equals(name)
                ).findFirst();
    }
}
