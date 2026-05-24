package com.example.comot.permission;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.permission.application.ports.PermissionRepository;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.permission.domaine.viewModel.PermissionViewModel;
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
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreSQLTestConfiguration.class)
@Transactional
public class GetPermissionsE2ETest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PermissionRepository repository;

    @BeforeEach
    void setUp() {
        for (Category category: Category.values()) {
            var permission = new Permission(
                    "permission_" + category.toString(),
                    "title",
                    category,
                    "description"
            );

            repository.save(permission);
        }
    }

    @Test
    void shouldGetPermission() throws Exception {

        var result = mockMvc.perform(
                MockMvcRequestBuilders.get("/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var listPermissionViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<PermissionViewModel>>() {}
        );

        Assertions.assertEquals(listPermissionViewModel.size(), Category.values().length -1);
        Assertions.assertEquals(listPermissionViewModel.getFirst().getCategory(), Category.MANAGE_SCHEDULE);
    }
}
