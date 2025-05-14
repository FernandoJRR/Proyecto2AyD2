package com.ayd.reports_service.reservations.dto;

import java.util.List;

import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.Value;

@Value
public class ReportReservationsDTO {
    List<ReservationResponseDTO> reservations;
    Integer totalReservations;
}
