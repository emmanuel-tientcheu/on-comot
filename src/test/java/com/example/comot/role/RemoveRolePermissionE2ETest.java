package com.example.comot.role;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.infrastructure.spring.dto.AddRolePermissionDTO;
import com.example.comot.role.infrastructure.spring.dto.RemoveRolePermissionDTO;
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
public class RemoveRolePermissionE2ETest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RoleRepository repository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleQueries roleQueries;

    @Autowired
    EntityManager entityManager;

    Role role;

    @BeforeEach
    void setUp() {
        repository.clear();
        permissionRepository.clear();
        for (Category category : Category.values()) {
            var permission = new Permission(
                    "permission_" + category.toString(),
                    "title",
                    category,
                    "description"
            );

            permissionRepository.save(permission);
        }

        role = new Role(
                "1",
                "title",
                "description",
                true
        );

        var permission = permissionRepository.getAllPermissions().getFirst();
        role.addPermission(permission.getId(), permission.getCategory());

        this.repository.save(role);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldRemovePermission() throws Exception {
        var permission = this.permissionRepository.findByCategory(Category.MANAGE_SCHEDULE).get();

        var dto = new RemoveRolePermissionDTO(
                role.getId(),
                permission.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/roles/remove-permission")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var role = this.repository.findById(dto.getRoleId()).get();

        Assertions.assertFalse(role.hasPermission(permission.getCategory()));
    }

    @Test
    void whenPermissionDoesNotAssigned_shouldThrow() throws Exception {
        var permission = this.permissionRepository.findByCategory(Category.CREATE_EVENT).get();

        var dto = new RemoveRolePermissionDTO(
                role.getId(),
                permission.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/roles/remove-permission")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

    }
}
