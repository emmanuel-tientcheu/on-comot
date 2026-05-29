package com.example.comot.organisation.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.organisation.application.useCases.*;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationsViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;
import com.example.comot.organisation.infrastructure.spring.dto.*;
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

    @PatchMapping("/{id}")
    ResponseEntity<Void> updateOrganisation(
            @PathVariable("id") String id,
            @RequestBody UpdateOrganisationDTO dto
    ) {
        this.pipeline.send(new UpdateOrganisationCommand(id, dto.getName(), dto.getDescription()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrganisation(@PathVariable("id") String id) {
        var result = this.pipeline.send(new DeleteOrganisationCommand(id));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add-member")
    ResponseEntity<Void> addMemberToOrganisation(@RequestBody AddMemberToOrganisationDTO dto) {
        this.pipeline.send(new AddMemberToOrganisationCommand(
                dto.getOrganisationId(),
                dto.getUserId()
        ));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-member")
    ResponseEntity<Void> removeMemberFromOrganisation(@RequestBody RemoveMemberFromOrganisationDTO dto) {
        this.pipeline.send(new RemoveMemberFromOrganisationCommand(
                dto.getOrganisationId(),
                dto.getUserId()
        ));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add-permission-to-member")
    ResponseEntity<Void> addPermissionToMember(@RequestBody AddPermissionToMemberDTO dto) {
        var result = this.pipeline.send(new AddPermissionToMemberCommand(
                dto.getOrganisationId(),
                dto.getUserId(),
                dto.getPermissionId(),
                dto.getCategory()
        ));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove-permission-from-member")
    ResponseEntity<Void> removePermissionFromMember(@RequestBody RemovePermissionFromMemberDTO dto) {
        this.pipeline.send(new RemovePermissionFromMemberCommand(
                dto.getOrganisationId(),
                dto.getUserId(),
                dto.getPermissionId()
        ));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{organisationId}/members/{userId}")
    ResponseEntity<MemberOrganisationViewModel> getMember(
            @PathVariable("organisationId") String organisationId,
            @PathVariable("userId") String userId
    ) {
        var result = this.pipeline.send(new GetMemberCommand(organisationId, userId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{organisationId}/members")
    ResponseEntity<OrganisationMembersViewModel> getOrganisationMembers(
            @PathVariable("organisationId") String organisationId
    ) {
        var result = this.pipeline.send(new GetOrganisationMembersCommand(organisationId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/organisations")
    ResponseEntity<MemberOrganisationsViewModel> getUserOrganisations(
            @PathVariable("userId") String userId
    ) {
        var result = this.pipeline.send(new GetUserOrganisationsCommand(userId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
