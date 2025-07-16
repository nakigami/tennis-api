package com.bforbank.tennis_api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI documentation for the Tennis Scoring API.
 *
 * @return Custom OpenAPI configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tennis Scoring API")
                        .version("1.0.0")
                        .description("REST API for tennis score management for BforBank"));
    }
}