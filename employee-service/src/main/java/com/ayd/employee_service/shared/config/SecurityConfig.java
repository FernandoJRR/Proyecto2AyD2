package com.ayd.employee_service.shared.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ayd.employee_service.auth.jwt.filters.AuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final AppProperties appProperties;

        private final AuthenticationFilter authenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activa CORS
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/login")
                                                .permitAll()
                                                // vamos a resguardar las rutas con los permisos necesarios
                                                .anyRequest().authenticated() // Protege el resto de rutas
                                )
                                // sin sesiones
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.getOrBuild();
        }

        /**
         * Configuración de CORS personalizada
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                System.out.println(appProperties.getFrontURL());
                CorsConfiguration configuration = new CorsConfiguration();
                // agrega todas las rutas permitidas
                configuration.setAllowedOrigins(List.of(appProperties.getFrontURL(), appProperties.getGatewayURL()));

                // decimos que operaciones http estan permitidos
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

                // decimos que headers estan permitidos
                configuration.setAllowedHeaders(
                                List.of("Authorization", "Content-Type", "auth-user", "auth-permissions"));

                // permite cookies y credenciales
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                // aplicamos CORS a todas las rutas del sistema
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        /**
         * Configura el bean que sera expueto cuando se necesite el cripter en el
         * sistema, se eligio esta implementacion porque utiliza BCrypt (version 2B para
         * compatibilidad con
         * caracteres especiales) y 12 iteraciones en el algoritmo.
         *
         * @return
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(BCryptVersion.$2B, 12);
        }
}
