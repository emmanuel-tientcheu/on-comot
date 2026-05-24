package com.example.comot.core.domaine.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {
    @Id
    private String id;

    public BaseEntity() {}

    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
