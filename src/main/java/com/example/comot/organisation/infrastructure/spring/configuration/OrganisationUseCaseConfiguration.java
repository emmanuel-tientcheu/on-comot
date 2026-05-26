package com.example.comot.organisation.infrastructure.spring.configuration;

import com.example.comot.auth.application.ports.UserRepository;
import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.application.useCases.CreateOrganisationCommandHandler;
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
}
