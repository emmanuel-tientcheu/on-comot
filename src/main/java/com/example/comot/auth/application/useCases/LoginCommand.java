package com.example.comot.auth.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.LoginUserViewModel;

public class LoginCommand implements Command<LoginUserViewModel> {
    private String email;
    private String password;

    public LoginCommand() {}

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
