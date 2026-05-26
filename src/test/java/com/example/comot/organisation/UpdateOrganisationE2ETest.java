package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.UpdateOrganisationCommand;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.spring.dto.UpdateOrganisationDTO;
import jakarta.persistence.EntityManager;
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
public class UpdateOrganisationE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    Organisation organisation;

    User user;

    @BeforeEach
    void setUp() {
        organisationRepository.clear();

        user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        organisation = new Organisation(
                "org-1",
                "name",
                "description",
                true,
                user.getId()
        );

        userRepository.save(user);
        organisationRepository.save(organisation);

    }

    @Test
    void shouldUpdateRole() throws Exception {
        var dto = new UpdateOrganisationDTO(
                organisation.getId(),
                "new name",
                "new description"
        );

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/organisations/" + organisation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        var org = this.organisationRepository.findById(organisation.getId()).get();

        Assertions.assertEquals(org.getName(), dto.getName());
        Assertions.assertEquals(org.getDescription(), dto.getDescription());
    }
}
