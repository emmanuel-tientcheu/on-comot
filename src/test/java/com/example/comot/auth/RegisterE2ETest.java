package com.example.comot.auth;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.auth.infrastructure.spring.dto.RegisterUserDTO;
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
public class RegisterE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordHasher passwordHasher;


    @Test
    void shouldRegisterUser() throws Exception {

        var dto = new RegisterUserDTO(
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        var idResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                IdResponse.class
        );

        var userQuery = this.repository.findById(idResponse.getId());

        Assertions.assertTrue(userQuery.isPresent());
        var user = userQuery.get();

        Assertions.assertEquals(user.getFirstName(), dto.getFirstName());
        Assertions.assertEquals(user.getLastname(), dto.getLastname());
        Assertions.assertEquals(user.getEmail(), dto.getEmail());

        Assertions.assertTrue(passwordHasher.match(dto.getPassword(), user.getPassword()));
    }

    @Test
    void whenEmailAlreadyUse_shouldThrow() throws Exception {
        var user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        this.repository.save(user);

        var dto = new RegisterUserDTO(
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
