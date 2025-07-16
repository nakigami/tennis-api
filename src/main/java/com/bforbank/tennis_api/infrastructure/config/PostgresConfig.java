package com.bforbank.tennis_api.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.bforbank.tennis_api.adapter.out.persistence")
@EntityScan(basePackages = "com.bforbank.tennis_api.adapter.out.persistence")
public class PostgresConfig {
}
