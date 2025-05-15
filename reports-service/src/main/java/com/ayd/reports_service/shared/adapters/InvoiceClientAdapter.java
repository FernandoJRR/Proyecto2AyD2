package com.ayd.reports_service.shared.adapters;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.reports_service.shared.ports.InvoiceClientPort;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceClientAdapter implements InvoiceClientPort {

    private final WebClient.Builder webClientBuilder;
    private final String URL = "lb://API-GATEWAY/api/v1/invoices";

    @Override
    public List<InvoiceResponseDTO> getProfitsByIds(List<String> ids) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("lb")
                        .host("API-GATEWAY")
                        .path("/api/v1/invoices/byIds")
                        .queryParam("ids", ids.toArray())
                        .build())
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToFlux(InvoiceResponseDTO.class)
                // se confierte el flux en lista
                .collectList()
                // bloqueamos la ejecución hasta recibir el resultado completo
                .block();
    }
}
