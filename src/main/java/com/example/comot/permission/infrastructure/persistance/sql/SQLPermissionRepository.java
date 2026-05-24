package com.example.comot.permission.infrastructure.persistance.sql;

import com.example.comot.core.spring.persistance.sql.SQLBaseRepository;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class SQLPermissionRepository extends SQLBaseRepository<Permission> implements PermissionRepository {
    public SQLPermissionRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<Permission> getEntityMangerClass() {
        return Permission.class;
    }

    @Override
    public Optional<Permission> findByCategory(Category category) {
        var query = this.entityManager.createQuery(
               "SELECT DISTINCT p FROM Permission p WHERE p.category = :category", Permission.class
        );

        query.setParameter("category", category);

        return query.getResultList()
                .stream()
                .findFirst();

    }

    @Override
    public List<Permission> getAllPermissions() {
        var query = this.entityManager.createQuery(
                "SELECT DISTINCT p FROM Permission p WHERE p.category != :category", Permission.class
        );

        query.setParameter("category", Category.SUPER_ADMIN);

        return query.getResultList();
    }

}
