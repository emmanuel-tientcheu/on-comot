package com.example.comot.organisation.domaine.model;

import com.example.comot.auth.domaine.model.User;
import com.example.comot.core.domaine.model.BaseEntity;
import com.example.comot.permission.domaine.model.Category;
import com.example.comot.permission.domaine.model.Permission;
import com.example.comot.role.domaine.model.Role;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    private Set<OrganisationMember> members;

    public Organisation() {}

    public Organisation(String id, String name, String description, Boolean active, String userId) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.userId = userId;
        this.members = new HashSet<>();
    }

    public String getUserId() { return userId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getActive(){ return active; }

    public User getUser() { return  user; }

    public void setActive(Boolean active) { this.active = active; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void addMember(String userId) {
        if(this.members.stream().anyMatch(member -> member.getUserId().equals(userId))) {
            throw new IllegalArgumentException("This member is already in the organisation");
        }
        this.members.add(
                new OrganisationMember(
                        UUID.randomUUID().toString(),
                        userId,
                        this.getId()
                )
        );
    }

    public void removeMember(String userId) {
        if(this.members.stream().noneMatch(member -> member.getUserId().equals(userId))) {
            throw new IllegalArgumentException("This member is not in the organisation");
        }

        this.members.removeIf(member -> member.getUserId().equals(userId));
    }

    public boolean hasMember(String userId) {
        return  this.members.stream()
                .anyMatch(member -> member.getUserId().equals(userId));
    }

    public void addPermission(String userId, String permissionId, Category category) {
        var member = this.members.stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst();

        if(member.isEmpty()) {
            throw new IllegalArgumentException("This user is not in the organisation");
        }

        member.get().addPermission(permissionId, category);
    }

    public void removePermission(String userId, String permissionId) {
        var member = this.members.stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst();

        if(member.isEmpty()) {
            throw new IllegalArgumentException("This user is not in the organisation");
        }

        member.get().removePermission(permissionId);
    }

    public boolean hasPermission(String userId, String permissionId) {
        var member = this.members.stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst();

        if(member.isEmpty()) {
            throw new IllegalArgumentException("This member is already in the organisation");
        }

        return member.get().hasPermission(permissionId);
    }


    public static class OrganisationMember extends BaseEntity {
        private String userId;
        private String organisationId;
        private Set<PermissionMember> permissions;

        public OrganisationMember(){}

        public OrganisationMember(String id, String userId, String organisationId) {
            super(id);
            this.userId = userId;
            this.organisationId = organisationId;
            this.permissions = new HashSet<>();
        }


        public String getUserId() { return userId; }

        public String getOrganisationId() { return organisationId; }

        public Set<PermissionMember> getPermissions() { return permissions; }

        public void addPermission(String permissionId, Category category) {
            if(this.permissions.stream().anyMatch(p -> p.getPermissionId().equals(permissionId))) {
                throw new IllegalArgumentException("This permission is already assign to this member");
            }
            this.permissions.add(
                    new PermissionMember(
                            UUID.randomUUID().toString(),
                            this.getId(),
                            permissionId,
                            category
                    )
            );
        }

        public void removePermission(String permissionId) {
            if(this.permissions.stream().noneMatch(p -> p.getPermissionId().equals(permissionId))) {
                throw new IllegalArgumentException("This user have not this permission");
            }

            this.permissions.removeIf(
                    p-> p.getPermissionId().equals(permissionId)
            );
        }

        public boolean hasPermission(String permissionId) {
            return this.permissions
                    .stream()
                    .anyMatch( p -> p.getPermissionId().equals(permissionId));
        }

        public static class PermissionMember extends BaseEntity{
            private String organisationMemberId;
            private String permissionId;
            private Category category;

            public PermissionMember() {}

            public PermissionMember(String id, String orgMemberId, String permissionId, Category category) {
                super(id);
                this.organisationMemberId = orgMemberId;
                this.permissionId = permissionId;
                this.category = category;
            }

            public String getPermissionId() { return permissionId; }

            public Category getCategory() { return category; }

            public String getOrganisationMemberId() { return organisationMemberId; }
        }
    }
}
