package com.example.comot.auth.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.application.exceptions.NotFoundException;
import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.auth.application.services.jwtService.JWTService;
import com.example.comot.auth.application.services.passwordHasher.PasswordHasher;
import com.example.comot.auth.domaine.viewModel.LoginUserViewModel;
import com.example.comot.core.application.exceptions.BadRequestException;

public class LoginCommandHandler implements Command.Handler<LoginCommand, LoginUserViewModel> {
    private final UserRepository repository;
    private final PasswordHasher passwordHasher;
    private final JWTService jwtService;

    public LoginCommandHandler(UserRepository repository, PasswordHasher passwordHasher, JWTService jwtService) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }

    @Override
    public LoginUserViewModel handle(LoginCommand command) {

        var user = this.repository.findByEmail(command.getEmail()).orElseThrow(
                ()-> new NotFoundException("User", command.getEmail())
        );

        if(!passwordHasher.match(command.getPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        return new LoginUserViewModel(
                user.getId(),
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                this.jwtService.tokenize(user)
        );
    }
}
