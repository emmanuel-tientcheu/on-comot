package com.example.comot.organisation.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommand;
import com.example.comot.organisation.infrastructure.spring.dto.CreateOrganisationDTO;
import com.example.comot.permission.infrastructure.spring.dto.CreatePermissionDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organisations")
@Transactional
public class OrganisationController {

    private final Pipeline pipeline;

    public OrganisationController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    ResponseEntity<IdResponse> createOrganisation(@RequestBody CreateOrganisationDTO dto) {
        var result = this.pipeline.send(new CreateOrganisationCommand(
                dto.getName(),
                dto.getDescription(),
                dto.getActive(),
                dto.getUserId()
        ));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
