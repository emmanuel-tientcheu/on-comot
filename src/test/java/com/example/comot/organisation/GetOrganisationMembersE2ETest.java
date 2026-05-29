package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.OrganisationMembersViewModel;
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
public class GetOrganisationMembersE2ETest {
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

    private User owner;
    private User member1;
    private User member2;
    private Organisation organisation;

    @BeforeEach
    void setUp() {
        userRepository.clear();
        organisationRepository.clear();

        owner = new User(
                "owner-1",
                "Emmanuel",
                "Keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        member1 = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        member2 = new User(
                "member-2",
                "Jane",
                "Smith",
                "jane.smith@gmail.com",
                "password"
        );

        organisation = new Organisation(
                "org-1",
                "My Organisation",
                "Description",
                true,
                owner.getId()
        );

        userRepository.save(owner);
        userRepository.save(member1);
        userRepository.save(member2);
        organisationRepository.save(organisation);

        organisation.addMember(member1.getId());
        organisation.addMember(member2.getId());
        organisationRepository.save(organisation);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldGetOrganisationMembers() throws Exception {
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/organisations/{organisationId}/members", organisation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var membersViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrganisationMembersViewModel.class
        );

        // Vérifications de l'organisation
        Assertions.assertEquals(organisation.getId(), membersViewModel.getId());
        Assertions.assertEquals(organisation.getName(), membersViewModel.getName());
        Assertions.assertEquals(organisation.getDescription(), membersViewModel.getDescription());

        // Vérifications des membres (2 membres)
        Assertions.assertEquals(2, membersViewModel.getMembers().size());

        // Vérification du premier membre (member1)
        var firstMember = membersViewModel.getMembers().stream()
                .filter(m -> m.getUserId().equals(member1.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(firstMember);
        Assertions.assertEquals(member1.getId(), firstMember.getUserId());
        Assertions.assertEquals(member1.getEmail(), firstMember.getEmail());
        Assertions.assertEquals(member1.getFirstName(), firstMember.getFirstName());
        Assertions.assertEquals(member1.getLastname(), firstMember.getLastName());

        // Vérification du deuxième membre (member2)
        var secondMember = membersViewModel.getMembers().stream()
                .filter(m -> m.getUserId().equals(member2.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(secondMember);
        Assertions.assertEquals(member2.getId(), secondMember.getUserId());
        Assertions.assertEquals(member2.getEmail(), secondMember.getEmail());
        Assertions.assertEquals(member2.getFirstName(), secondMember.getFirstName());
        Assertions.assertEquals(member2.getLastname(), secondMember.getLastName());
    }

    @Test
    void whenOrganisationNotFound_shouldThrowNotFound() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/organisations/{organisationId}/members", "non-existent-org")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
