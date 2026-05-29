package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommand;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.spring.dto.CreateOrganisationDTO;
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
public class CreateOrganisationE2ETest {

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

    User user;
    Permission permission;

    @BeforeEach
    void setUp() {
        userRepository.clear();
        permissionRepository.clear();

        user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

         permission = new Permission(
                "perm-1",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        userRepository.save(user);
        permissionRepository.save(permission);
    }



    @Test
    void shouldCreateOrganisation() throws Exception{
        var dto = new CreateOrganisationDTO(
                "name",
                "description",
                true,
                user.getId()
        );


        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        var idResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                IdResponse.class
        );

        var orgQuery = organisationRepository.findById(idResponse.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var organisation = orgQuery.get();

        Assertions.assertEquals(organisation.getName(), dto.getName());
        Assertions.assertEquals(organisation.getDescription(), dto.getDescription());
        Assertions.assertTrue(organisation.getActive());
        Assertions.assertEquals(organisation.getUserId(), dto.getUserId());
    }

    @Test
    void whenNameIdAlreadyUser_shouldThrow() throws Exception {

        var organisation = new Organisation(
                "org-1",
                "name",
                "description",
                true,
                user.getId()
        );

        this.organisationRepository.save(organisation);

        var dto = new CreateOrganisationDTO(
                "name",
                "description",
                true,
                user.getId()
        );


        mockMvc.perform(
                MockMvcRequestBuilders.post("/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }
}
