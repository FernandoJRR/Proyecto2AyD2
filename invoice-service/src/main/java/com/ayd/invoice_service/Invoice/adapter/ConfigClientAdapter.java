package com.ayd.invoice_service.Invoice.adapter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.invoice_service.Invoice.ports.ConfigClientPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfigClientAdapter implements ConfigClientPort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public ParameterResponseDTO getRegimenParameter() throws NotFoundException {
        return webClientBuilder.build()
                .get()
                .uri("lb://API-GATEWAY/api/v1/config/regimen")
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(message -> new NotFoundException(message)))
                .bodyToMono(ParameterResponseDTO.class)
                .block();
    }

}
