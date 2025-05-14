package com.ayd.reports_service.popularhours.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/popular-hours-report")
@RequiredArgsConstructor
public class PopularHoursReportController {

    private final ReportServicePort<PopularHoursReportDTO, PeriodRequestDTO> reservationReportPort;

    @Operation(summary = "Crear un reporte de horas populares")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PopularHoursReportDTO getPopularHoursBetweenDates(
            @Valid @RequestBody PeriodRequestDTO filters) {
        PopularHoursReportDTO reservations = (PopularHoursReportDTO) reservationReportPort.generateReport(filters);
        return reservations;
    }

    @Operation(summary = "Exporta un reporte de horas populares a pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte exportado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportPopularHoursBetweenDates(@Valid @RequestBody PeriodRequestDTO filters)
            throws ReportGenerationExeption {
        byte[] pdfBytes = reservationReportPort.generateReportAsPdf(filters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_horas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
