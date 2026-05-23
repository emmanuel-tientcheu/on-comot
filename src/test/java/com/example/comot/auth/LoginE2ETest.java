package com.example.comot.auth;

import com.example.comot.PostgreSQLTestConfiguration;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.domaine.viewModel.LoginUserViewModel;
import com.example.comot.auth.infrastructure.spring.dto.LoginDTO;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreSQLTestConfiguration.class)
@Transactional
public class LoginE2ETest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordHasher passwordHasher;

    @Autowired
    JWTService jwtService;

    User user;


    @BeforeEach
    void setUp() {
         user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                passwordHasher.encode("password")
        );
        repository.clear();
        repository.save(user);
    }

    @Test
    void shouldLogUser() throws Exception {
        var dto = new LoginDTO(
                "emmanuelkeou@gmail.com",
                "password"
        );

        var result = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var logUserViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                LoginUserViewModel.class
        );

        Assertions.assertNotNull(logUserViewModel);

        Assertions.assertEquals(logUserViewModel.getId(), user.getId());
        Assertions.assertEquals(logUserViewModel.getFirstName(), user.getFirstName());
        Assertions.assertEquals(logUserViewModel.getLastname(), user.getLastname());
        Assertions.assertEquals(logUserViewModel.getEmail(), user.getEmail());

        var authUser = jwtService.parse(logUserViewModel.getToken());
        Assertions.assertNotNull(authUser);

        Assertions.assertNotNull(authUser);

        Assertions.assertEquals(authUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(authUser.getLastname(), user.getLastname());
        Assertions.assertEquals(authUser.getEmail(), user.getEmail());
    }
}
