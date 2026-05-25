package com.example.comot.role;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.infrastructure.spring.dto.CreateRoleDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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
public class CreateRoleE2ETest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RoleRepository repository;

    @Test
    void shouldCreateRole() throws Exception {
        var dto = new CreateRoleDTO(
                "title",
                "description",
                true
        );

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        var idResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                IdResponse.class
        );

        var roleQuery = repository.findById(idResponse.getId());
        Assertions.assertTrue(roleQuery.isPresent());

        var role = roleQuery.get();
        Assertions.assertEquals(role.getTitle(), dto.getTitle());
        Assertions.assertEquals(role.isDefault(), dto.isDefault());
        Assertions.assertTrue(role.isDefault());

    }
}
