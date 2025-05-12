package com.ayd.shared.security;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    /**
     * COnfigura el web client para que use el balanceador de spring cloud.
     * 
     * @return
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(jwtPropagationFilter());
    }

    /**
     * Agrega el JWT del contexto de seguridad al encabezado Authorization de las
     * solicitudes WebClient.
     * Si no hay autenticación, la solicitud se envía sin modificaciones.
     */
    private ExchangeFilterFunction jwtPropagationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                ClientRequest modifiedRequest = ClientRequest.from(request)
                        .header("Authorization", "Bearer " + authentication.getCredentials())
                        .build();
                return Mono.just(modifiedRequest);
            }

            return Mono.just(request);
        });
    }
}
