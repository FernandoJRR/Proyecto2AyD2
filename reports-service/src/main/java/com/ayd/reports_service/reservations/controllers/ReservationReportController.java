package com.ayd.reports_service.reservations.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.shared.services.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservation-reports")
@RequiredArgsConstructor
public class ReservationReportController {

    private final ReportServicePort<PeriodRequestDTO> reservationReportPort;

    @Operation(summary = "Crear un reporte de reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ReportReservationsDTO createReservationReport(
            @Valid @RequestBody PeriodRequestDTO filters) {
        ReportReservationsDTO reservations = reservationReportPort.generateReport(filters);
        return reservations;
    }
}
