package com.example.comot.organisation.infrastructure.spring.configuration;

import com.example.comot.organisation.application.ports.OrganisationRepository;
import com.example.comot.organisation.infrastructure.persistance.ram.InMemoryOrganisationRepository;
import com.example.comot.organisation.infrastructure.persistance.sql.SQLOrganisationAccessor;
import com.example.comot.organisation.infrastructure.persistance.sql.SQLOrganisationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrganisationAdapterConfiguration {

    @Bean
    public OrganisationRepository organisationRepository(
            EntityManager entityManager,
            SQLOrganisationAccessor dataAccessor
    ) {
        return new SQLOrganisationRepository(entityManager, dataAccessor);
    }
}
