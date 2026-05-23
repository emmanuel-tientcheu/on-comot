package com.example.comot.auth;

import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.jwtService.ConcreteJWTService;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.application.services.passwordHasher.BcryptPasswordHasher;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.application.useCases.LoginCommand;
import com.example.comot.auth.application.useCases.LoginCommandHandler;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import com.example.comot.core.application.exceptions.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LoginTest {
    private final UserRepository repository = new InMemoryUserRepository();
    private final PasswordHasher hasher = new BcryptPasswordHasher();
    private final JWTService jwtService = new ConcreteJWTService(
            "this_is_my_fake_secret_key_please_dont_share",
            60
    );
    User user = new User(
            "user-1",
            "emmanuel",
            "keou",
            "emmanuelkeou@gmail.com",
            hasher.encode("password")
    );

    private final LoginCommandHandler commandHandler() {
        return new LoginCommandHandler(repository, hasher, jwtService);
    }

    @BeforeEach
    void setUp() {
        repository.clear();
        repository.save(user);
    }

    @Nested
    class Scenario_HappyPath {
        @Test
        void shouldLogin() {

            var command = new LoginCommand(
                    "emmanuelkeou@gmail.com",
                    "password"
            );

            var useCase = commandHandler();

            var result = useCase.handle(command);

            Assertions.assertEquals(result.getId(), user.getId());
            Assertions.assertEquals(result.getFirstName(), user.getFirstName());
            Assertions.assertEquals(result.getLastname(), user.getLastname());
            Assertions.assertEquals(result.getEmail(), user.getEmail());

            var authUser = jwtService.parse(result.getToken());
            Assertions.assertNotNull(authUser);

            Assertions.assertEquals(authUser.getFirstName(), user.getFirstName());
            Assertions.assertEquals(authUser.getLastname(), user.getLastname());
            Assertions.assertEquals(authUser.getEmail(), user.getEmail());
        }
    }

    @Nested
    class Scenario_TheEmailIsIncorrect {
        @Test
        void shouldThrowNotFound() {
            var command = new LoginCommand(
                    "incorrectemail@gmail.com",
                    "password"
            );

            var useCase = commandHandler();

           var exception = Assertions.assertThrows(
                   NotFoundException.class,
                   ()-> useCase.handle(command)
           );

           Assertions.assertEquals("User with the key incorrectemail@gmail.com was not found", exception.getMessage());
        }
    }

    @Nested
    class Scenario_ThePasswordIsIncorrect {
        @Test
        void shouldThrowBadRequest() {
            var command = new LoginCommand(
                    "emmanuelkeou@gmail.com",
                    "incorrectPassword"
            );

            var useCase = commandHandler();

            var exception = Assertions.assertThrows(
                    BadRequestException.class,
                    ()-> useCase.handle(command)
            );

            Assertions.assertEquals("Incorrect password", exception.getMessage());
        }
    }

}
