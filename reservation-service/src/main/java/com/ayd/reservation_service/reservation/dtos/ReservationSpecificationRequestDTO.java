package com.ayd.reservation_service.reservation.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Value;

@Value
public class ReservationSpecificationRequestDTO {
    private String userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private boolean online;
    private boolean paid;
    private boolean cancelled;
}
