package com.ayd.reservation_service.reservation.adapters;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.reservation_service.reservation.ports.ForInvoiceClientPort;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceClientAdapter implements ForInvoiceClientPort {

    private final WebClient.Builder webClientBuilder;
    private final String URL = "lb://API-GATEWAY/api/v1/invoices";

    @Override
    public InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceDetailRequestDTO) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.POST)
                .uri(URL)
                .bodyValue(createInvoiceDetailRequestDTO)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToMono(InvoiceResponseDTO.class).block();
    }

    @Override
    public InvoiceResponseDTO getInvoice(String invoiceId) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .get()
                .uri(URL + "/" + invoiceId)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToMono(InvoiceResponseDTO.class).block();
    }
}
