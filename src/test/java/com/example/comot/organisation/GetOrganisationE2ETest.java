package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.OrganisationViewModel;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreSQLTestConfiguration.class)
@Transactional
public class GetOrganisationE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    @Autowired
    EntityManager entityManager;

    User user;

    Organisation organisation;

    @BeforeEach
    void setUp() {
        userRepository.clear();
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

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldGetOrganisation() throws Exception {
        var result = mockMvc.perform(
                MockMvcRequestBuilders.get("/organisations/" + organisation.getId())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var orgViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrganisationViewModel.class
        );

        Assertions.assertEquals(orgViewModel.getName(), organisation.getName());
        Assertions.assertEquals(orgViewModel.getDescription(), organisation.getDescription());
        Assertions.assertEquals(orgViewModel.getActive(), organisation.getActive());
        Assertions.assertEquals(orgViewModel.getUser().getId(), organisation.getUserId());

    }
}
