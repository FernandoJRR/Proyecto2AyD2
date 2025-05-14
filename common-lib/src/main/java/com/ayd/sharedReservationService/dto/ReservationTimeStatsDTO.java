package com.ayd.sharedReservationService.dto;

import java.time.LocalTime;

public class ReservationTimeStatsDTO {

    private LocalTime startTime;
    private LocalTime endTime;
    private Long total;

    public ReservationTimeStatsDTO(LocalTime startTime, LocalTime endTime, Long total) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.total = total;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Long getTotal() {
        return total;
    }
}
