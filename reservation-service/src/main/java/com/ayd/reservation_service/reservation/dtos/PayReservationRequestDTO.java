package com.ayd.reservation_service.reservation.dtos;

import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class PayReservationRequestDTO {

    @NotBlank(message = "Id de la reservacion no puede ser vacio")
    String reservationId;

    @NotNull(message = "Los datos de facturacion no pueden estar nulos")
    @Valid
    CreateInvoiceRequestDTO createInvoiceRequestDTO;
}
