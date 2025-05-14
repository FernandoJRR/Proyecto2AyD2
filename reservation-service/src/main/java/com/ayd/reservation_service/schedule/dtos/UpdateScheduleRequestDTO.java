package com.ayd.reservation_service.schedule.dtos;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UpdateScheduleRequestDTO {

    @NotNull(message = "La hora de inicio es requerida")
    private LocalTime startTime;
    @NotNull(message = "La hora de fin es requerida")
    private LocalTime endTime;
}
