package com.example.comot.permission.domaine.model;

import com.example.comot.core.domaine.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @Column(nullable = true)
    private String description;

    public Permission() {}

    public Permission(String id, String title, Category category, String description) {
        super(id);
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
