package com.ayd.reports_service.popularhours.dtos;

import java.util.List;

import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

import lombok.Value;

@Value
public class PopularHoursReportDTO {

    List<ReservationTimeStatsDTO> popularHours;
    Integer totalReservations;
}
