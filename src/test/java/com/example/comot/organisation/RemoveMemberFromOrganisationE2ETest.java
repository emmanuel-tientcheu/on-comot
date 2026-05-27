package com.example.comot.organisation;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.domaine.model.Organisation;
import com.example.comot.organisation.infrastructure.spring.dto.RemoveMemberFromOrganisationDTO;
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
public class RemoveMemberFromOrganisationE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    User owner;
    User memberToRemove;
    User otherMember;
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

        memberToRemove = new User(
                "member-1",
                "John",
                "Doe",
                "john.doe@gmail.com",
                "password"
        );

        otherMember = new User(
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
        userRepository.save(memberToRemove);
        userRepository.save(otherMember);

        organisationRepository.save(organisation);

        organisation.addMember(memberToRemove.getId());
        organisation.addMember(otherMember.getId());
        organisation.addMember(owner.getId());
        organisationRepository.save(organisation);
    }

    @Test
    void shouldRemoveMemberFromOrganisation() throws Exception {
        var dto = new RemoveMemberFromOrganisationDTO(
                organisation.getId(),
                memberToRemove.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/organisations/remove-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        var orgQuery = organisationRepository.findById(organisation.getId());
        Assertions.assertTrue(orgQuery.isPresent());

        var updatedOrganisation = orgQuery.get();
        Assertions.assertFalse(updatedOrganisation.hasMember(memberToRemove.getId()));
        Assertions.assertTrue(updatedOrganisation.hasMember(otherMember.getId()));
        Assertions.assertEquals(2, updatedOrganisation.getMembers().size());
    }

    @Test
    void whenMemberNotFound_shouldThrow() throws Exception {
        var dto = new RemoveMemberFromOrganisationDTO(
                organisation.getId(),
                "non-existent-user"
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/organisations/remove-member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

//    @Test
//    void whenTryToRemoveOwner_shouldThrow() throws Exception {
//        var dto = new RemoveMemberFromOrganisationDTO(
//                organisation.getId(),
//                owner.getId()
//        );
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.delete("/organisations/remove-member")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(dto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//
//        // Vérifier que le propriétaire est toujours là (pas dans les membres)
//        var orgQuery = organisationRepository.findById(organisation.getId());
//        Assertions.assertTrue(orgQuery.isPresent());
//        Assertions.assertFalse(orgQuery.get().hasMember(owner.getId()));
//    }
    
}
