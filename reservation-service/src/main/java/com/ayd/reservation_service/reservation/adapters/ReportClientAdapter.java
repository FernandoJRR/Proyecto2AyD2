package com.ayd.reservation_service.reservation.adapters;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.reservation_service.reservation.ports.ForReportClientPort;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportClientAdapter implements ForReportClientPort {

    private final WebClient.Builder webClientBuilder;

    private final static String URL = "lb://API-GATEWAY/api/v1/reservations-exports";

    @Override
    public byte[] exportInvoiceWithQR(ReservationInterServiceDTO reservationInterServiceDTO) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.POST)
                .uri(URL + "/invoice-qr")
                .bodyValue(reservationInterServiceDTO)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToMono(byte[].class).block();
    }

    @Override
    public byte[] exportReservationTicket(ReservationResponseDTO reservationInterServiceDTO) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.POST)
                .uri(URL + "/reservation-ticket")
                .bodyValue(reservationInterServiceDTO)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToMono(byte[].class).block();
    }
}
