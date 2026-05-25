package com.example.comot.role.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.role.application.useCases.AddRolePermissionCommand;
import com.example.comot.role.application.useCases.CreateRoleCommand;
import com.example.comot.role.application.useCases.GetRoleCommand;
import com.example.comot.role.domaine.viewModel.RoleViewModel;
import com.example.comot.role.infrastructure.spring.dto.AddRolePermissionDTO;
import com.example.comot.role.infrastructure.spring.dto.CreateRoleDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@Transactional
public class RoleController {

    private final Pipeline pipeline;

    public RoleController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    ResponseEntity<IdResponse> createRole(@RequestBody CreateRoleDTO dto) {
        var result = this.pipeline.send(new CreateRoleCommand(
                dto.getTitle(),
                dto.getDescription(),
                dto.isDefault()
        ));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<RoleViewModel> getRole(@PathVariable("id") String id) {
        var result = this.pipeline.send(new GetRoleCommand(id));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add-permission")
    ResponseEntity<Void> addPermission(@RequestBody AddRolePermissionDTO dto) {
        this.pipeline.send(new AddRolePermissionCommand(dto.getRoleId(), dto.getPermissionId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
