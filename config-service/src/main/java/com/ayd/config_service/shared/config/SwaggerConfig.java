package com.ayd.config_service.shared.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("API de Hospital")
                        .version("1.0")
                        .description("Documentación de la API para el proyecto de hospital"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public") // grupo de endpoints públicos
                .pathsToMatch("/api/**") // se define el patrón de las rutas a documentar
                .build();
    }
}
