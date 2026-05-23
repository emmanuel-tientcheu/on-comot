package com.example.comot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLTestConfiguration {

	public static void main(String[] args) {
		SpringApplication.from(ComotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		return new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
