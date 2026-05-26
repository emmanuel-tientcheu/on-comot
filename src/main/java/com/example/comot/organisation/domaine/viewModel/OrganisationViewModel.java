package com.example.comot.organisation.domaine.viewModel;

public class OrganisationViewModel {
    private String id;
    private String name;
    private String description;
    private Boolean active;
    private OrgUser user;

    public OrganisationViewModel() {}

    public OrganisationViewModel(String id, String name, String description, Boolean active, OrgUser user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.user = user;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getActive(){ return active; }

    public OrgUser getUser() { return user; }

    public static class OrgUser {
        private String id;
        private String firstName;
        private String lastname;
        private String email;

        public OrgUser() {}

        public OrgUser(String id, String firstName, String lastname, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastname = lastname;
            this.email = email;
        }

        public String getId() { return id; }

        public String getFirstName() { return firstName; }

        public String getLastname() { return lastname; }

        public String getEmail() { return email; }

    }
}
