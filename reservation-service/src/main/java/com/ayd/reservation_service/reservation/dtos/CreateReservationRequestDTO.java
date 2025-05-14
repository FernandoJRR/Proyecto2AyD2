package com.ayd.reservation_service.reservation.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateReservationRequestDTO {
    @NotNull(message = "La hora de inicio es requerida")
    private LocalTime startTime;
    @NotNull(message = "La hora de fin es requerida")
    private LocalTime endTime;
    @NotNull(message = "La fecha es requerida")
    private LocalDate date;

    private String customerCUI;

    private String packageId;




}
