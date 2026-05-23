package com.example.comot.auth.application.useCases;

import an.awesome.pipelinr.Command;
import com.example.comot.auth.domaine.viewModel.IdResponse;

public class RegisterCommand implements Command<IdResponse> {
    private String firstName;
    private String lastname;
    private String email;
    private String password;

    public RegisterCommand() {}

    public RegisterCommand(String firstName, String lastname, String email, String password) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
