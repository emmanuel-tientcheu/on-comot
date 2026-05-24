package com.example.comot.permission.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.application.useCases.CreatePermissionCommand;
import com.example.comot.permission.application.useCases.GetPermissionsHandler;
import com.example.comot.permission.domaine.viewModel.PermissionViewModel;
import com.example.comot.permission.infrastructure.spring.dto.CreatePermissionDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Transactional
public class PermissionController {
    private final Pipeline pipeline;
    private final GetPermissionsHandler permissionsHandler;

    public PermissionController(Pipeline pipeline, GetPermissionsHandler permissionsHandler) {
        this.pipeline = pipeline;
        this.permissionsHandler = permissionsHandler;
    }

    @GetMapping
    ResponseEntity<List<PermissionViewModel>> getPermissions() {
        var result = this.permissionsHandler.handle();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<IdResponse> createPermission(@RequestBody CreatePermissionDTO dto) {
        var result = this.pipeline.send(new CreatePermissionCommand(
                dto.getTitle(),
                dto.getCategory(),
                dto.getDescription()
        ));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
