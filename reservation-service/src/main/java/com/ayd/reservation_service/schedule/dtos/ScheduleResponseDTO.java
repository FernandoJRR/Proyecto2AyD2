package com.ayd.reservation_service.schedule.dtos;

import java.time.LocalTime;

import lombok.Value;

@Value
public class ScheduleResponseDTO {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean online;
}
