package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.domaine.viewModel.MemberOrganisationsViewModel;
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
public class GetUserOrganisationsE2ETest  {

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

    private User user;
    private User otherUser;
    private Organisation organisationAsMember1;
    private Organisation organisationAsMember2;
    private Organisation otherOrganisation;

    @BeforeEach
    void setUp() {
        userRepository.clear();
        organisationRepository.clear();

        user = new User(
                "user-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        otherUser = new User(
                "other-user-1",
                "Jane",
                "Smith",
                "jane.smith@gmail.com",
                "password"
        );

        // Organisation 1 : où l'utilisateur est membre
        organisationAsMember1 = new Organisation(
                "org-1",
                "Member Organisation 1",
                "First organisation where user is member",
                true,
                otherUser.getId()
        );

        // Organisation 2 : où l'utilisateur est membre aussi
        organisationAsMember2 = new Organisation(
                "org-2",
                "Member Organisation 2",
                "Second organisation where user is member",
                true,
                otherUser.getId()
        );


        // Autre organisation (ni membre ni propriétaire)
        otherOrganisation = new Organisation(
                "org-4",
                "Other Organisation",
                "User has no relation",
                true,
                otherUser.getId()
        );

        userRepository.save(user);
        userRepository.save(otherUser);

        organisationRepository.save(organisationAsMember1);
        organisationRepository.save(organisationAsMember2);
        organisationRepository.save(otherOrganisation);

        // Ajouter l'utilisateur comme membre des deux organisations
        organisationAsMember1.addMember(user.getId());
        organisationAsMember2.addMember(user.getId());
        organisationRepository.save(organisationAsMember1);
        organisationRepository.save(organisationAsMember2);

        entityManager.clear();
        entityManager.clear();
    }

    @Test
    void shouldGetUserOrganisations() throws Exception {
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/organisations/users/"+  user.getId() +"/organisations")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                MemberOrganisationsViewModel.class
        );

        // Vérifications de l'utilisateur
        Assertions.assertEquals(user.getId(), response.getId());
        Assertions.assertEquals(user.getFirstName(), response.getFirstName());
        Assertions.assertEquals(user.getLastname(), response.getLastName());
        Assertions.assertEquals(user.getEmail(), response.getEmail());

        // Vérifications des organisations (devrait avoir 2 organisations)
        Assertions.assertEquals(2, response.getOrganisations().size());

        var orgIds = response.getOrganisations().stream()
                .map(MemberOrganisationsViewModel.Organisation::getOrgId)
                .toList();

        Assertions.assertTrue(orgIds.contains(organisationAsMember1.getId()));
        Assertions.assertTrue(orgIds.contains(organisationAsMember2.getId()));
    }

}
