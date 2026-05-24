package com.example.comot.auth.domaine.model;

import com.example.comot.core.domaine.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column
    private String firstName;
    @Column
    private String lastname;
    @Column
    private String email;
    @Column
    private String password;

    public User() {}

    public User(String id, String firstName, String lastname, String email, String password) {
        super(id);
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
