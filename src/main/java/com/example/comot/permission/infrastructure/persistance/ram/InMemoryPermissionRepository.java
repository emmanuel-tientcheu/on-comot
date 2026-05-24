package com.example.comot.permission.infrastructure.persistance.ram;

import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;

import java.util.List;
import java.util.Optional;

public class InMemoryPermissionRepository extends InMemoryBaseRepository<Permission> implements PermissionRepository {

    @Override
    public Optional<Permission> findByCategory(Category category) {
        return
                this.entities.values()
                        .stream()
                        .filter(permission -> permission.getCategory().equals(category))
                        .findFirst();
    }

    @Override
    public List<Permission> getAllPermissions() {
        return this.entities.values()
                .stream()
                .filter(permission -> !permission.getCategory().equals(Category.SUPER_ADMIN) )
                .toList();
    }
}
