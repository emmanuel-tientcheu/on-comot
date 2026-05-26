package com.example.comot.organisation.infrastructure.spring.configuration;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.organisation.application.ports.OrganisationQueries;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommandHandler;
import com.example.comot.organisation.application.useCases.DeleteOrganisationCommandHandler;
import com.example.comot.organisation.application.useCases.GetOrganisationCommandHandler;
import com.example.comot.organisation.application.useCases.UpdateOrganisationCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganisationUseCaseConfiguration {

    @Bean
    public CreateOrganisationCommandHandler createOrganisationCommandHandler(
            final OrganisationRepository organisationRepository,
            final UserRepository userRepository
    ) {
        return new CreateOrganisationCommandHandler(organisationRepository, userRepository);
    }

    @Bean
    public GetOrganisationCommandHandler getOrganisationCommandHandler(
            final OrganisationQueries organisationQueries
    ) {
        return new GetOrganisationCommandHandler(organisationQueries);
    }

    @Bean
    public UpdateOrganisationCommandHandler updateOrganisationCommandHandler(
            final OrganisationRepository organisationRepository
    ) {
        return new UpdateOrganisationCommandHandler(organisationRepository);
    }

    @Bean
    public DeleteOrganisationCommandHandler deleteOrganisationCommandHandler(
            final OrganisationRepository organisationRepository
    ) {
        return new DeleteOrganisationCommandHandler(organisationRepository);
    }
}
