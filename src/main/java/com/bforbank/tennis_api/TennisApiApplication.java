package com.bforbank.tennis_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.bforbank.tennis_api.infrastructure.adapter.repository.springdata")
@EntityScan(basePackages = {
		"com.bforbank.tennis_api.domain.model",
		"com.bforbank.tennis_api.infrastructure.adapter.repository.entity"
})
public class TennisApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisApiApplication.class, args);
	}

}
