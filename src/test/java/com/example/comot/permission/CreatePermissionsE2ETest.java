package com.example.comot.permission;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.infrastructure.spring.controller.PermissionController;
import com.example.comot.permission.infrastructure.spring.dto.CreatePermissionDTO;
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
public class CreatePermissionsE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PermissionRepository repository;

    @Test
    void shouldCreatePermission() throws Exception {
        var dto = new CreatePermissionDTO(
                "title",
                "MANAGE_SCHEDULE",
                "description"
        );

        var result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        var idResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                IdResponse.class
        );

        var permissionQuery = this.repository.findById(idResponse.getId());
        Assertions.assertFalse(permissionQuery.isEmpty());

        var permission = permissionQuery.get();

        Assertions.assertEquals(permission.getTitle(), dto.getTitle());
        Assertions.assertEquals(permission.getCategory(), dto.getCategory());
        Assertions.assertEquals(permission.getDescription(), dto.getDescription());

    }

    @Test
    void whenPermissionAlreadyExists_shouldThrow() throws Exception {

        var permission = new Permission(
                "permission-1",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        this.repository.save(permission);

        var dto = new CreatePermissionDTO(
                "title",
                "CREATE_EVENT",
                "description"
        );

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/permissions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }
}
