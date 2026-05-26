package com.example.comot.organisation.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommand;
import com.example.comot.organisation.application.useCases.GetOrganisationCommand;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;
import com.example.comot.organisation.infrastructure.spring.dto.CreateOrganisationDTO;
import com.example.comot.permission.infrastructure.spring.dto.CreatePermissionDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    ResponseEntity<OrganisationViewModel> getOrganisation(@PathVariable("id") String id) {
        var result = this.pipeline.send(new GetOrganisationCommand(id));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
