package com.ayd.sharedReservationService.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Value;

@Value
public class ReservationResponseDTO {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Boolean notShow;
    private Boolean paid;
    private String gameId;
    private String customerFullName;
    private String customerNIT;
}
