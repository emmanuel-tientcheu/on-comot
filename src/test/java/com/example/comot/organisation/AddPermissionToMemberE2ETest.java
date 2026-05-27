package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.spring.dto.AddPermissionToMemberDTO;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreSQLTestConfiguration.class)
@Transactional
public class AddPermissionToMemberE2ETest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    PermissionRepository permissionRepository;

    private User owner;
    private User member;
    private Organisation organisation;
    private Permission permission;

    @BeforeEach
    void setUp() {
        userRepository.clear();
        organisationRepository.clear();
        permissionRepository.clear();

        owner = new User(
                "owner-1",
                "Emmanuel",
                "Keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        member = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        permission = new Permission(
                "perm-1",
                "View Sales",
                Category.VIEW_SALES,
                "Can view sales"
        );

        organisation = new Organisation(
                "org-1",
                "My Organisation",
                "Description",
                true,
                owner.getId()
        );

        userRepository.save(owner);
        userRepository.save(member);
        permissionRepository.save(permission);
        organisationRepository.save(organisation);
        organisation.addMember(member.getId());
        organisationRepository.save(organisation);
    }

    @Test
    void shouldAddPermissionToMember() throws Exception {
        var dto = new AddPermissionToMemberDTO(
                organisation.getId(),
                member.getId(),
                permission.getId(),
                permission.getCategory().toString()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/organisations/add-permission-to-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        var orgQuery = organisationRepository.findById(organisation.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var updatedOrganisation = orgQuery.get();
        Assertions.assertTrue(updatedOrganisation.hasPermission(member.getId(), permission.getId()));
    }

    @Test
    void whenMemberNotInOrganisation_shouldThrow() throws Exception {
        var nonMember = new User(
                "non-member-1",
                "Jane",
                "Doe",
                "jane@test.com",
                "password"
        );
        userRepository.save(nonMember);

        var dto = new AddPermissionToMemberDTO(
                organisation.getId(),
                nonMember.getId(),
                permission.getId(),
                permission.getCategory().toString()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/organisations/add-permission-to-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenPermissionAlreadyAssigned_shouldThrow() throws Exception {
        organisation.addPermission(member.getId(), permission.getId(), permission.getCategory());
        organisationRepository.save(organisation);

        var dto = new AddPermissionToMemberDTO(
                organisation.getId(),
                member.getId(),
                permission.getId(),
                permission.getCategory().toString()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/organisations/add-permission-to-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
