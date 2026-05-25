package com.example.comot.role.infrastructure.persistance.sql;

import com.example.comot.core.spring.persistance.sql.SQLBaseRepository;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.domaine.model.Role;
import jakarta.persistence.EntityManager;

public class SQLRoleRepository extends SQLBaseRepository<Role> implements RoleRepository {

    public SQLRoleRepository(final EntityManager entityManager) {
       super(entityManager);
    }

    @Override
    public Class<Role> getEntityMangerClass() {
        return Role.class;
    }
}
