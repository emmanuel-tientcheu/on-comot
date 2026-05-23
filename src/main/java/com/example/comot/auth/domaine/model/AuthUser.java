package com.example.comot.auth.domaine.model;


public class AuthUser {
    private String id;
    private String firstName;
    private String lastname;
    private String email;

    public AuthUser() {}

    public AuthUser(String id, String firstName, String lastname, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
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
}
