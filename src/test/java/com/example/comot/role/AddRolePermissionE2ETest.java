package com.example.comot.role;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.role.application.ports.RoleQueries;
import com.example.comot.role.application.ports.RoleRepository;
import com.example.comot.role.domaine.model.Role;
import com.example.comot.role.domaine.viewModel.RoleViewModel;
import com.example.comot.role.infrastructure.spring.dto.AddRolePermissionDTO;
import com.example.comot.role.infrastructure.spring.dto.CreateRoleDTO;
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
public class AddRolePermissionE2ETest {

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
    void shouldAddPermission() throws Exception {
        var permission = new Permission(
                "permission-1",
                "title",
                Category.CREATE_EVENT,
                "description"
        );

        this.permissionRepository.save(permission);


        var dto = new AddRolePermissionDTO(
                role.getId(),
                permission.getId()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/roles/add-permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var role = this.repository.findById(dto.getRoleId()).get();

        Assertions.assertTrue(role.hasPermission(permission.getCategory()));
    }

    @Test
    void whenPermissionAlreadyAssign_shouldThrow() throws Exception {
        var permission = new Permission(
                "permission-2",
                "title",
                Category.CREATE_EVENT,
                "description"
        );
        this.permissionRepository.save(permission);

        var existingRole = this.repository.findById(role.getId()).get();
        existingRole.addPermission(permission.getId(), permission.getCategory());
        this.repository.save(existingRole);

        var dto = new AddRolePermissionDTO(
                role.getId(),
                permission.getId()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/roles/add-permission")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldGetRole() throws Exception {

        var result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/roles/" + role.getId())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var roleViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                RoleViewModel.class
        );

        var rolePermission = roleViewModel.getPermissions().getFirst();
        var permission = permissionRepository.getAllPermissions().getFirst();

        Assertions.assertEquals(rolePermission.getPermissionId(), permission.getId());
        Assertions.assertEquals(rolePermission.getTitle(), permission.getTitle());

    }
}
