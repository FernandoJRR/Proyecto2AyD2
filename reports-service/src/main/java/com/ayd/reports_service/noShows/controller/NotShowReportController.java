package com.ayd.reports_service.noShows.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.reports_service.noShows.ports.NotShowReportPort;
import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservation-not-show")
public class NotShowReportController {

    @Qualifier("notShowReportService")
    @Autowired
    private NotShowReportPort notShowReportPort;

    @Operation(summary = "Crear un reporte de reservas en donde el cliente no se presernto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ReportReservationsDTO createNotShowReport(
            @Valid @RequestBody PeriodRequestDTO filters) {
        ReportReservationsDTO reservations = (ReportReservationsDTO) notShowReportPort.generateReport(filters);
        return reservations;
    }

    @Operation(summary = "Exporta un reporte de reservas donde el cliente no se presento a pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte exportado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportNotShowReport(@Valid @RequestBody PeriodRequestDTO filters)
            throws ReportGenerationExeption {
        byte[] pdfBytes = notShowReportPort.generateReportAsPdf(filters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_reservas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
