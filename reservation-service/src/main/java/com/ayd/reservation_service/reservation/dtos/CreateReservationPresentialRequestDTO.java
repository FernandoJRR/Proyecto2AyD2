package com.ayd.reservation_service.reservation.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateReservationPresentialRequestDTO extends CreateReservationDTO {

    @NotNull(message = "Los datos de facturacion no pueden estar nulos")
    @Valid
    private CreateInvoiceRequestDTO createInvoiceRequestDTO;


    public CreateReservationPresentialRequestDTO(@NotNull(message = "La hora de inicio es requerida") LocalTime startTime,
            @NotNull(message = "La hora de fin es requerida") LocalTime endTime,
            @NotNull(message = "La fecha es requerida") LocalDate date,
            @NotBlank(message = "El NIT del cliente es requerido") String customerNIT,
            @NotBlank(message = "El nombre del cliente es requerido") String customerFullName,
            @Valid CreateInvoiceRequestDTO createInvoiceRequestDTO) {
        super(startTime, endTime, date, customerNIT, customerFullName);
        this.createInvoiceRequestDTO = createInvoiceRequestDTO;
    }

}
