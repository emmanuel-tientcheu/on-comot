package com.example.comot.organisation.infrastructure.persistance.sql;

import com.example.comot.core.spring.persistance.sql.SQLBaseRepository;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SQLOrganisationRepository extends SQLBaseRepository<Organisation> implements OrganisationRepository {

    private final SQLOrganisationAccessor dataAccessor;

    public SQLOrganisationRepository(EntityManager entityManager, SQLOrganisationAccessor dataAccessor) {
        super(entityManager);
        this.dataAccessor = dataAccessor;
    }
    @Override
    public Optional<Organisation> findByUserId(String userId) {
        return this.dataAccessor.findByUserId(userId);
    }

    @Override
    public Optional<Organisation> findByName(String name) {
        return this.dataAccessor.findByName(name);
    }

    @Override
    public Class<Organisation> getEntityMangerClass() {
        return Organisation.class;
    }
}
