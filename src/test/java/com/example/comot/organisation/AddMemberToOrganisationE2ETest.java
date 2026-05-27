package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.spring.dto.AddMemberToOrganisationDTO;
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
public class AddMemberToOrganisationE2ETest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    User owner;
    User memberToAdd;
    Organisation organisation;

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

        memberToAdd = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
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
        userRepository.save(memberToAdd);
        organisationRepository.save(organisation);
    }

    @Test
    void shouldAddMemberToOrganisation() throws Exception {
        var dto = new AddMemberToOrganisationDTO(
                organisation.getId(),
                memberToAdd.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/organisations/add-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var orgQuery = organisationRepository.findById(organisation.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var updatedOrganisation = orgQuery.get();
        Assertions.assertTrue(updatedOrganisation.hasMember(memberToAdd.getId()));
    }

    @Test
    void whenMemberAlreadyInOrganisation_shouldThrow() throws Exception {
        organisation.addMember(memberToAdd.getId());
        organisationRepository.save(organisation);

        var dto = new AddMemberToOrganisationDTO(
                organisation.getId(),
                memberToAdd.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/organisations/add-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }
    
}
