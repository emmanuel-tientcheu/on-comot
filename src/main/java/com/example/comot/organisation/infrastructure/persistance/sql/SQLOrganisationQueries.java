package com.example.comot.organisation.infrastructure.persistance.sql;

import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SQLOrganisationQueries implements OrganisationQueries {
    private final EntityManager entityManager;

    public SQLOrganisationQueries(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public OrganisationViewModel findById(String id) {
        var query = this.entityManager.createQuery(
                "SELECT o FROM Organisation o " +
                    "JOIN FETCH o.user " +
                        "WHERE o.id = :id"
                , Organisation.class
        );

        query.setParameter("id", id);

        var result = query.getSingleResult();

        var user = result.getUser();

        return new OrganisationViewModel(
                result.getId(),
                result.getName(),
                result.getDescription(),
                result.getActive(),
                new OrganisationViewModel.OrgUser(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastname(),
                        user.getEmail()
                )
        );
    }
}
