package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationViewModel;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
public class GetMemberE2ETest {

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

    @Autowired
    EntityManager entityManager;

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
                "Can view sales reports"
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

        organisation.addPermission(member.getId(), permission.getId(), permission.getCategory());
        organisationRepository.save(organisation);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldGetMemberWithPermissions() throws Exception {
        var result = mockMvc.perform(
                MockMvcRequestBuilders.get("/organisations/{organisationId}/members/{userId}",
                                organisation.getId(),
                                member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var memberOrganisation = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                MemberOrganisationViewModel.class
        );

        Assertions.assertEquals(memberOrganisation.getOrganisationId(), organisation.getId());
        Assertions.assertEquals(memberOrganisation.getOrganisationName(), organisation.getName());

        Assertions.assertEquals(memberOrganisation.getUserId(), member.getId());
        Assertions.assertEquals(memberOrganisation.getEmail(), member.getEmail());
        Assertions.assertEquals(memberOrganisation.getFirstName(), member.getFirstName());
        Assertions.assertEquals(memberOrganisation.getLastName(), member.getLastname());

        var permissionMember = memberOrganisation.getPermissions().getFirst();

        Assertions.assertEquals(permissionMember.getPermissionId(), permission.getId());
        Assertions.assertEquals(permissionMember.getTitle(), permission.getTitle());
        Assertions.assertEquals(permissionMember.getDescription(), permission.getDescription());
        Assertions.assertEquals(permissionMember.getCategory(), permission.getCategory());


    }
}
