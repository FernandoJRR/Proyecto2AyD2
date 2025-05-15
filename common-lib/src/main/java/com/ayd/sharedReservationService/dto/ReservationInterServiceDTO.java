package com.ayd.sharedReservationService.dto;

import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ReservationInterServiceDTO {
    @Valid
    @NotNull(message = "La informacion de la reserva no puede ser nula")
    ReservationResponseDTO reservationResponseDTO;

    @Valid
    @NotNull(message = "La informacion de la factura no puede ser nula")
    InvoiceResponseDTO invoiceResponseDTO;
}
