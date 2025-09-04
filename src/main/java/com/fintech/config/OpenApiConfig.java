package com.fintech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Account Management Service API")
                        .description("A RESTful web service for managing account data with CRUD operations," +
                                " phone number validation, and error handling."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api/v1")
                                .description("Development Server")));
    }
}