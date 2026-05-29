package com.example.comot.organisation.infrastructure.persistance.sql;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationsViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    @Override
    public Optional<MemberOrganisationViewModel> getMember(String orgId, String userId) {
        try {
            var query = this.entityManager.createQuery(
                    "SELECT DISTINCT m FROM com.example.comot.organisation.domaine.model.Organisation$OrganisationMember m " +
                            "JOIN FETCH m.organisation o " +
                            "JOIN FETCH m.user u " +
                            "LEFT JOIN FETCH m.permissions p " +
                            "LEFT JOIN FETCH p.permission " +
                            "WHERE o.id = :orgId AND m.userId = :userId",
                    Organisation.OrganisationMember.class
            );

            query.setParameter("orgId", orgId);
            query.setParameter("userId", userId);

            return query.getResultList().stream().findFirst().map(member -> {
                var permissions = member.getPermissions().stream()
                        .map(p -> new MemberOrganisationViewModel.MemberPermission(
                                p.getId(),
                                p.getPermissionId(),
                                p.getPermission() != null ? p.getPermission().getTitle() : null,
                                p.getPermission() != null ? p.getPermission().getDescription() : null,
                                p.getCategory()
                        ))
                        .toList();

                return new MemberOrganisationViewModel(
                        member.getId(),
                        member.getOrganisation().getId(),
                        member.getOrganisation().getName(),
                        member.getUserId(),
                        member.getUser().getEmail(),
                        member.getUser().getFirstName(),
                        member.getUser().getLastname(),
                        permissions
                );
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    public OrganisationMembersViewModel getMembers(String orgId) {
        try {
            var query = this.entityManager.createQuery(
                    "SELECT DISTINCT o FROM Organisation o "
                            + "LEFT JOIN FETCH o.members m "
                            + "LEFT JOIN FETCH m.user "
                            + "WHERE o.id = :orgId",
                    Organisation.class
            );

            query.setParameter("orgId", orgId);

            var result = query.getSingleResult();

            var members = result.getMembers()
                    .stream().map(member -> {
                        return new OrganisationMembersViewModel.Member(
                                member.getId(),
                                member.getUserId(),
                                member.getUser() != null ? member.getUser().getEmail() : null,
                                member.getUser() != null ? member.getUser().getFirstName() : null,
                                member.getUser() != null ? member.getUser().getLastname() : null
                        );
                    }).toList();

            return new OrganisationMembersViewModel(
                    result.getId(),
                    result.getName(),
                    result.getDescription(),
                    members
            );
        } catch (NoResultException e) {
            throw new NotFoundException("Organisation", orgId);
        }
    }

    public MemberOrganisationsViewModel getOrganisationsUser(String userId) {
        var query = this.entityManager.createQuery(
                "SELECT DISTINCT m FROM com.example.comot.organisation.domaine.model.Organisation$OrganisationMember m "
                        + "JOIN FETCH m.user "
                        + "JOIN FETCH m.organisation "
                        + "WHERE m.userId = :userId",
                Organisation.OrganisationMember.class
        );

        query.setParameter("userId", userId);

        var members = query.getResultList();
        if (members.isEmpty()) {
            throw new NotFoundException("User", userId);
        }

        var organisations = members.stream()
                .map(member -> {
                    return new MemberOrganisationsViewModel.Organisation(
                            member.getOrganisationId(),
                            member.getOrganisation().getName(),
                            member.getOrganisation().getDescription()
                    );
                }).toList();
        var user = members.getFirst().getUser();

        return new MemberOrganisationsViewModel(
                user.getId(),
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                organisations
        );
    }
}
