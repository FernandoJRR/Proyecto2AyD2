package com.ayd.reservation_service.reservation.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends Auditor {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private boolean online;

    private boolean paid = false;

    private boolean cancelled = false;

    public Reservation(CreateReservationRequestDTO createReservationRequestDTO) {
        this.startTime = createReservationRequestDTO.getStartTime();
        this.endTime = createReservationRequestDTO.getEndTime();
        this.date = createReservationRequestDTO.getDate();
        this.userId = createReservationRequestDTO.getUserId();
        this.online = createReservationRequestDTO.isOnline();
        this.paid = false;
        this.cancelled = false;
    }
}
