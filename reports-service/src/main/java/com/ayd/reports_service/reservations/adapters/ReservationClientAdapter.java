package com.ayd.reports_service.reservations.adapters;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.reports_service.reservations.port.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationClientAdapter implements ReservationClientPort {
    private final WebClient.Builder webClientBuilder;

    /**
     * Obtiene un listado de reservaciones desde el microservicio de reservas a
     * través del API Gateway,
     * filtrando por el período de tiempo y otros criterios especificados.
     *
     * Este método realiza una solicitud HTTP GET al endpoint
     * /api/v1/reservations/all
     * 
     * @param filters DTO con los filtros deseados: fechas, tipo de período,
     *                usuario, estado de pago, etc.
     * @return lista de reservas que cumplen con los criterios indicados
     */
    @Override
    public List<ReservationResponseDTO> getReservationReportByPeriod(PeriodRequestDTO filters) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.GET)
                .uri("lb://API-GATEWAY/api/v1/reservations/getReservationsBetweenDates")
                .bodyValue(filters)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToFlux(ReservationResponseDTO.class)
                // se confierte el flux en lista
                .collectList()
                // bloqueamos la ejecución hasta recibir el resultado completo
                .block();
    }

}
