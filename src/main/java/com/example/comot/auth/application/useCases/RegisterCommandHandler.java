package com.example.comot.auth.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.EmailUnavailableException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.domaine.model.User;
import com.example.comot.auth.domaine.viewModel.IdResponse;

import java.util.UUID;

public class RegisterCommandHandler implements Command.Handler<RegisterCommand, IdResponse> {
    private final UserRepository repository;
    private final PasswordHasher hasher;

    public RegisterCommandHandler(UserRepository repository, PasswordHasher hasher) {
        this.repository = repository;
        this.hasher = hasher;
    }

    @Override
    public IdResponse handle(RegisterCommand command) {

        this.repository.findByEmail(command.getEmail()).ifPresent(
                user -> {
                    throw new EmailUnavailableException();
                }
        );

        var user = new User(
                UUID.randomUUID().toString(),
                command.getFirstName(),
                command.getLastname(),
                command.getEmail(),
                this.hasher.encode(command.getPassword())
        );
        this.repository.save(user);

        return new IdResponse(user.getId());
    }
}
