package com.ayd.reservation_service.reservation.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

public interface ReservationRepository
        extends JpaRepository<Reservation, String>, JpaSpecificationExecutor<Reservation> {

    @Query("""
            SELECT c FROM Reservation c
            WHERE (:startDate IS NULL OR c.date >= :startDate)
              AND (:endDate IS NULL OR c.date <= :endDate)
            """)
    public List<Reservation> findReservationByDateBetween(
            LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT new com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO(r.startTime, r.endTime, COUNT(r))
                FROM Reservation r
                WHERE (:startDate IS NULL OR r.date >= :startDate)
                  AND (:endDate IS NULL OR r.date <= :endDate)
                GROUP BY r.startTime, r.endTime
                ORDER BY COUNT(r) DESC
            """)
    public List<ReservationTimeStatsDTO> findReservationsGroupedByTimeRangeAndFilteredByDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    public boolean existsByStartTimeAndEndTimeAndDate(
            LocalTime startTime, LocalTime endTime, LocalDate date);



}
