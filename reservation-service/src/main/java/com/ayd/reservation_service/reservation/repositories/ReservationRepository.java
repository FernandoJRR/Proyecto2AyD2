package com.ayd.reservation_service.reservation.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.reservation_service.reservation.models.Reservation;

public interface ReservationRepository
                extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {

        boolean existsByStartTimeAndEndTimeAndDateAndUserIdAndOnline(
                        LocalTime startTime, LocalTime endTime, LocalDate date, String userId, boolean online);

        boolean existsByStartTimeAndEndTimeAndDateAndOnline(
                        LocalTime startTime, LocalTime endTime, LocalDate date, boolean online);

}
