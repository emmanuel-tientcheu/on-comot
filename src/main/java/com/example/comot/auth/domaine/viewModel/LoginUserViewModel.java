package com.example.comot.auth.domaine.viewModel;

public class LoginUserViewModel {
    private String id;
    private String firstName;
    private String lastname;
    private String email;
    private String token;

    public LoginUserViewModel() {}

    public LoginUserViewModel(String id, String firstName, String lastname, String email, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
        this.token = token;
    }

    public String getId() {
        return id;
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

    public String getToken() {
        return token;
    }
}
