package com.example.comot.organisation.domaine.viewModel;

import java.util.List;

public class MemberOrganisationsViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private List<Organisation> organisations;

    public MemberOrganisationsViewModel() {}

    public MemberOrganisationsViewModel(String id, String firstName, String lastName, String email, List<Organisation> organisations) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.organisations = organisations;
    }

    public String getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public List<Organisation> getOrganisations() { return organisations; }

    public static class Organisation {
        private String orgId;
        private String name;
        private String description;

        public Organisation() {}

        public Organisation(String orgId, String name, String description) {
            this.orgId = orgId;
            this.name = name;
            this.description = description;
        }

        public String getOrgId() { return orgId; }

        public String getName() { return name; }

        public String getDescription() { return description; }
    }
}
