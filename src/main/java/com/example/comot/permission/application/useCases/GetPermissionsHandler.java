package com.example.comot.permission.application.useCases;

import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.viewModel.PermissionViewModel;

import java.util.List;

public class GetPermissionsHandler {
    private final PermissionRepository repository;

    public GetPermissionsHandler(PermissionRepository repository) {
        this.repository = repository;
    }

    public List<PermissionViewModel> handle() {
        return this.repository.getAllPermissions()
                .stream()
                .map(
                        permission -> new PermissionViewModel(
                                permission.getId(),
                                permission.getTitle(),
                                permission.getCategory(),
                                permission.getDescription()
                        )
                ).toList();
    }
}
