package com.example.comot.organisation.domaine.model;

import com.example.comot.auth.domaine.model.User;
import com.example.comot.core.domaine.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "organisations")
public class Organisation extends BaseEntity {
    @Column
    private String name;

    @Column(nullable = true)
    private String description;

    @Column
    private Boolean active;

    @Column(name = "user_id")
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @MapsId("userId")
    private User user;

    public Organisation() {}

    public Organisation(String id, String name, String description, Boolean active, String userId) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.userId = userId;
    }

    public String getUserId() { return userId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getActive(){ return active; }

    public void setActive(Boolean active) { this.active = active; }

    public User getUser() { return  user; }

}
