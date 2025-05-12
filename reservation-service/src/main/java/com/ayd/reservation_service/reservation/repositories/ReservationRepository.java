package com.ayd.reservation_service.reservation.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ayd.reservation_service.reservation.models.Reservation;

public interface ReservationRepository
                extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {

        @Query("""
                        SELECT c FROM Reservation c
                        WHERE (:startDate IS NULL OR c.date >= :startDate)
                          AND (:endDate IS NULL OR c.date <= :endDate)
                        """)
        public List<Reservation> findReservationByDateBetween(
                        LocalDate startDate, LocalDate endDate);

        boolean existsByStartTimeAndEndTimeAndDateAndUserIdAndOnline(
                        LocalTime startTime, LocalTime endTime, LocalDate date, String userId, boolean online);

        boolean existsByStartTimeAndEndTimeAndDateAndOnline(
                        LocalTime startTime, LocalTime endTime, LocalDate date, boolean online);

}
