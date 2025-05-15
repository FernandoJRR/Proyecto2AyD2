package com.ayd.reports_service.reservationExports.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.reports_service.reservationExports.ports.ForReservationExportsPort;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservations-exports")
@RequiredArgsConstructor
public class ReservationExportsController {

    private final ForReservationExportsPort forReservationExportsPort;

    @Operation(summary = "Exporta el dto a un pdf con un qr generado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF exportado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/invoice-qr")
    public ResponseEntity<byte[]> exportInvoiceWithQR(
            @Valid @RequestBody ReservationInterServiceDTO reservationInterServiceDTO)
            throws ReportGenerationExeption {
        byte[] pdfBytes = forReservationExportsPort.exportInvoiceWithQR(reservationInterServiceDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_horas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Exporta el dto a un pdf con un tocket qie represernta unsa reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF exportado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/reservation-ticket")
    public ResponseEntity<byte[]> exportReservationTicket(
            @Valid @RequestBody ReservationResponseDTO reservationInterServiceDTO)
            throws ReportGenerationExeption {
        byte[] pdfBytes = forReservationExportsPort.exportReservationTicket(reservationInterServiceDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_horas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
