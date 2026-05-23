package com.example.comot.auth.infrastructure.spring.dto;

public class RegisterUserDTO {
    private String firstName;
    private String lastname;
    private String email;
    private String password;

    public RegisterUserDTO() {}

    public RegisterUserDTO(String firstName, String lastname, String email, String password) {
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
