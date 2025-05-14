package com.ayd.reservation_service.reservation.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateReservationDTO {

    @NotNull(message = "La hora de inicio es requerida")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es requerida")
    private LocalTime endTime;

    @NotNull(message = "La fecha es requerida")
    private LocalDate date;

    @NotBlank(message = "El NIT del cliente es requerido")
    private String customerNit;

    @NotBlank(message = "El nombre del cliente es requerido")
    private String customerFullname;



    public CreateReservationDTO(@NotNull(message = "La hora de inicio es requerida") LocalTime startTime,
            @NotNull(message = "La hora de fin es requerida") LocalTime endTime,
            @NotNull(message = "La fecha es requerida") LocalDate date,
            @NotBlank(message = "El NIT del cliente es requerido") String customerNit,
            @NotBlank(message = "El nombre del cliente es requerido") String customerFullname) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.customerNit = customerNit;
        this.customerFullname = customerFullname;
    }

}
