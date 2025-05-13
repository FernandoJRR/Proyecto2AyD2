package com.ayd.sharedReservationService.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Value;

@Value
public class ReservationSpecificationRequestDTO {
    private String userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Boolean online;
    private Boolean paid;
    private Boolean cancelled;
}
