package com.ayd.reports_service.reservations.port;

import java.util.List;

import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

public interface ReservationClientPort {

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
    public List<ReservationResponseDTO> getReservationReportByPeriod(PeriodRequestDTO filters);
}
