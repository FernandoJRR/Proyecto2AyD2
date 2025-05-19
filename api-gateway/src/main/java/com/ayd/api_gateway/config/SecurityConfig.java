package com.ayd.api_gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AppProperties appProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {

        System.out.println(appProperties.getFrontURL());

        CorsConfiguration configuration = new CorsConfiguration();
        // agrega todas las rutas permitidas
        //configuration.setAllowedOrigins(List.of(appProperties.getFrontURL()));
        configuration.setAllowedOrigins(List.of("*"));


        // decimos que operaciones http estan permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // decimos que headers estan permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // permite cookies y credenciales
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // aplicamos CORS a todas las rutas del sistema
        source.registerCorsConfiguration("/**", configuration);

        return new CorsWebFilter(source);
    }

}
