package com.example.comot.organisation.domaine.viewModel;

import com.example.comot.permission.domaine.model.Category;

import java.util.List;

public class MemberOrganisationViewModel {
    private String id;

    private String organisationId;
    private String organisationName;

    private String userId;
    private String email;
    private String firstName;
    private String lastName;

    private List<MemberPermission> permissions;

    public MemberOrganisationViewModel() {}

    public MemberOrganisationViewModel(String id, String organisationId, String organisationName,
                                       String userId, String email,
                                       String firstName, String lastName, List<MemberPermission> permissions) {
        this.id = id;
        this.organisationId = organisationId;
        this.organisationName = organisationName;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.permissions = permissions;
    }

    public String getId() { return id; }

    public String getOrganisationId() { return organisationId; }

    public String getOrganisationName() { return organisationName; }

    public String getUserId() { return userId; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public List<MemberPermission> getPermissions() { return permissions; }

    public static class MemberPermission {
        private String id;
        private String permissionId;
        private String title;
        private String description;
        private Category category;

        public MemberPermission() {}

        public MemberPermission(String id, String permissionId, String title, String description, Category category) {
            this.id = id;
            this.permissionId = permissionId;
            this.title = title;
            this.description = description;
            this.category = category;
        }

        public String getId() { return id; }

        public String getPermissionId() { return permissionId; }

        public String getTitle() { return title; }

        public String getDescription() { return description; }

        public Category getCategory() { return category; }

    }

}
