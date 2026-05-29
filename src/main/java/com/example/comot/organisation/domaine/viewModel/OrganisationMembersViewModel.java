package com.example.comot.organisation.domaine.viewModel;

import java.util.List;

public class OrganisationMembersViewModel {
    private String id;
    private String name;
    private String description;
    private List<Member> members;

    public OrganisationMembersViewModel() {}

    public OrganisationMembersViewModel(String id, String name, String description, List<Member> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.members = members;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public List<Member> getMembers() { return members; }

    public static class Member {
        private String id;
        private String userId;
        private String email;
        private String firstName;
        private String lastName;

        public Member() {}

        public Member(String id, String userId, String email, String firstName, String lastName) {
            this.id = id;
            this.userId = userId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getId() { return id; }

        public String getUserId() { return userId; }

        public String getEmail() { return email; }

        public String getFirstName() { return firstName; }

        public String getLastName() { return lastName; }
    }
}
