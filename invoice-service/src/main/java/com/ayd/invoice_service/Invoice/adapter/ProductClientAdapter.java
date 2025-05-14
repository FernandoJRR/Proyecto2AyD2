package com.ayd.invoice_service.Invoice.adapter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.invoice_service.Invoice.ports.ProductClientPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClientPort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public GolfPackageResponseDTO getPackageById(String packageId) throws NotFoundException {
        return webClientBuilder.build()
                .get()
                .uri("lb://API-GATEWAY/api/v1/packages/" + packageId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(message -> new NotFoundException(message)))
                .bodyToMono(GolfPackageResponseDTO.class)
                .block();
    }

}
