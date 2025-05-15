package com.ayd.reports_service.shared.adapters;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

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

    /**
     * Realiza una solicitud HTTP POST al servicio de reservas para obtener los
     * rangos
     * horarios más populares dentro de un período de fechas especificado.
     * 
     * El método envía un objeto {@link PeriodRequestDTO} en el cuerpo de la
     * solicitud
     * y espera una lista de objetos {@link ReservationTimeStatsDTO} como respuesta.
     * 
     * El endpoint consultado es:
     * /api/v1/reservations/get-popular-hours-between-dates</code>
     * a través del API Gateway
     *
     * 
     * @param filters objeto que contiene la fecha de inicio y fin del período a
     *                consultar.
     * @return una lista de {@link ReservationTimeStatsDTO} que representan los
     *         rangos horarios con más reservas.
     */
    public List<ReservationTimeStatsDTO> getPopularHoursBetweenDates(PeriodRequestDTO filters) {
        return webClientBuilder.build()
                // usamos usa method en lugar de get para permitir enviar un body en la
                // solicitud
                .method(HttpMethod.POST)
                .uri("lb://API-GATEWAY/api/v1/reservations/get-popular-hours-between-dates")
                .bodyValue(filters)
                // ejecutmaos la solicitud y se prepara para obtener la respuesta
                .retrieve()
                // flux espera una lista
                .bodyToFlux(ReservationTimeStatsDTO.class)
                // se confierte el flux en lista
                .collectList()
                // bloqueamos la ejecución hasta recibir el resultado completo
                .block();
    }
}
