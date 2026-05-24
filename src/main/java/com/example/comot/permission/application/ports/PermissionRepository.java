package com.example.comot.permission.application.ports;

import com.example.comot.core.application.ports.BaseRepository;
import com.example.comot.core.spring.persistance.ram.InMemoryBaseRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission> {
    public Optional<Permission> findByCategory(Category category);
    public List<Permission> getAllPermissions();
}
