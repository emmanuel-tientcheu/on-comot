package com.example.comot.auth;

import com.example.comot.auth.application.exceptions.EmailUnavailableException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.passwordHasher.BcryptPasswordHasher;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.application.useCases.RegisterCommand;
import com.example.comot.auth.application.useCases.RegisterCommandHandler;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.infrastructure.persistance.ram.InMemoryUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTest {
    UserRepository repository = new InMemoryUserRepository();
    PasswordHasher hasher = new BcryptPasswordHasher();

    private RegisterCommandHandler commandHandler() {
        return new RegisterCommandHandler(repository, hasher);
    }
    @Test
    void shouldRegister() {

        var useCase = commandHandler();
        var command = new RegisterCommand(
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        var idResponse = useCase.handle(command);

        var userExpected = repository.findById(idResponse.getId());
        Assertions.assertTrue(userExpected.isPresent());

        var user = userExpected.get();

        Assertions.assertEquals(user.getId(), idResponse.getId());

        Assertions.assertTrue(
                hasher.match("password", user.getPassword())
        );
    }

    @Test
    void whenEmailAlreadyUser_shouldThrow() {
        var user = new User(
                "user-1",
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        this.repository.save(user);
        var useCase = commandHandler();
        var command = new RegisterCommand(
                "emmanuel",
                "keou",
                "emmanuelkeou@gmail.com",
                "password"
        );

        var exception = Assertions.assertThrows(
                EmailUnavailableException.class,
                () -> useCase.handle(command)
        );

        Assertions.assertEquals("this email is already use", exception.getMessage());
    }
}
