package com.example.comot.auth.infrastructure.spring.controller;

import an.awesome.pipelinr.Pipeline;
import com.example.comot.auth.application.useCases.LoginCommand;
import com.example.comot.auth.application.useCases.RegisterCommand;
import com.example.comot.auth.domaine.viewModel.IdResponse;
import com.example.comot.auth.domaine.viewModel.LoginUserViewModel;
import com.example.comot.auth.infrastructure.spring.dto.LoginDTO;
import com.example.comot.auth.infrastructure.spring.dto.RegisterUserDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Transactional
public class AuthController {

    private final Pipeline pipeline;

    public AuthController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/register")
    ResponseEntity<IdResponse> register(@RequestBody RegisterUserDTO dto) {
        var result = this.pipeline.send(new RegisterCommand(
                dto.getFirstName(),
                dto.getLastname(),
                dto.getEmail(),
                dto.getPassword()
        ));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginUserViewModel> login(@RequestBody LoginDTO dto) {
        var result = this.pipeline.send(new LoginCommand(
                dto.getEmail(),
                dto.getPassword()
        ));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
