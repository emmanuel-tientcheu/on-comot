package com.example.comot.role.infrastructure.persistance.ram;

import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.domaine.model.Role;

public class InMemoryRoleRepository extends InMemoryBaseRepository<Role> implements RoleRepository {
}
