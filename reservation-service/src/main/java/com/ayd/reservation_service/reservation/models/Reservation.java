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
    private Boolean paid;

    @Column(nullable = false)
    private Boolean notShow;

    @Column(nullable = false)
    private String customerNit;

    @Column(nullable = false)
    private String customerFullname;

    @Column(nullable = true)
    private String gameId;

    @Column(nullable = true)
    private String packageId;

    public Reservation(CreateReservationRequestDTO createReservationRequestDTO) {
        this.startTime = createReservationRequestDTO.getStartTime();
        this.endTime = createReservationRequestDTO.getEndTime();
        this.date = createReservationRequestDTO.getDate();
        this.paid = false;
        notShow = false;
        customerFullname = createReservationRequestDTO.getCustomerFullName();
        customerNit = createReservationRequestDTO.getCustomerNIT();
        packageId = createReservationRequestDTO.getPackageId();
    }
}
