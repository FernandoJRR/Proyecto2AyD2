package com.ayd.reservation_service.reservation.services;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.reservation_service.reservation.ports.ForReservationPort;
import com.ayd.reservation_service.reservation.repositories.ReservationRepository;
import com.ayd.reservation_service.reservation.specifications.ReservationSpecification;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedReservationService.dto.ReservationSpecificationRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class ReservationService implements ForReservationPort {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException, IllegalStateException {
        if (reservationRepository.existsByStartTimeAndEndTimeAndDate(
                createReservationRequestDTO.getStartTime(), createReservationRequestDTO.getEndTime(),
                createReservationRequestDTO.getDate())) {
            throw new DuplicatedEntryException("Ya existe una reserva con la misma configuración por el usuario.");
        }



        Reservation reservation = new Reservation(createReservationRequestDTO);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancelReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
        if (reservation.isPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }
        if (reservation.isCancelled()) {
            throw new IllegalStateException("La reserva ya ha sido cancelada.");
        }
        reservation.setCancelled(true);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation setPaymentReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
        if (reservation.isCancelled()) {
            throw new IllegalStateException("La reserva ha sido cancelada.");
        }
        if (reservation.isPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }
        reservation.setPaid(true);
        return reservationRepository.save(reservation);
    }

    @Override
    public boolean deleteReservation(String reservationId) throws IllegalStateException, NotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
        if (reservation.isPaid()) {
            throw new IllegalStateException("La reserva ya ha sido pagada.");
        }
        reservationRepository.delete(reservation);
        return true;
    }

    @Override
    public List<Reservation> getReservations(ReservationSpecificationRequestDTO reservationSpecificationRequestDTO) {
        if (reservationSpecificationRequestDTO == null) {
            return reservationRepository.findAll();
        }
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.hasStartTime(reservationSpecificationRequestDTO.getStartTime()))
                .and(ReservationSpecification.hasEndTime(reservationSpecificationRequestDTO.getEndTime()))
                .and(ReservationSpecification.hasDate(reservationSpecificationRequestDTO.getDate()))
                .and(ReservationSpecification.hasUserId(reservationSpecificationRequestDTO.getUserId()))
                .and(ReservationSpecification.isOnline(reservationSpecificationRequestDTO.getOnline()))
                .and(ReservationSpecification.isPaid(reservationSpecificationRequestDTO.getPaid()))
                .and(ReservationSpecification.isCancelled(reservationSpecificationRequestDTO.getCancelled()));
        return reservationRepository.findAll(spec);
    }

    /**
     * Obtiene todas las reservas que se encuentran dentro del rango de fechas
     * especificado.
     *
     * @param periodRequestDTO objeto que contiene la fecha de inicio y la fecha de
     *                         fin para el filtro.
     * @return una lista de objetos {@link Reservation} que tienen una fecha dentro
     *         del rango proporcionado.
     */
    @Override
    public List<Reservation> getReservationsBetweenDates(PeriodRequestDTO periodRequestDTO) {
        return reservationRepository.findReservationByDateBetween(periodRequestDTO.getStartDate(),
                periodRequestDTO.getEndDate());
    }

    /**
     * Obtiene una lista de rangos horarios con la cantidad de reservas realizadas
     * en cada uno,
     * dentro del rango de fechas especificado.
     *
     * @param periodRequestDTO objeto que contiene la fecha de inicio y la fecha de
     *                         fin para el filtro.
     * @return una lista de {@link ReservationTimeStatsDTO} que representan los
     *         rangos horarios más populares.
     */
    @Override
    public List<ReservationTimeStatsDTO> getPopularHoursBetweenDates(PeriodRequestDTO periodRequestDTO) {
        return reservationRepository.findReservationsGroupedByTimeRangeAndFilteredByDate(
                periodRequestDTO.getStartDate(), periodRequestDTO.getEndDate());
    }

    @Override
    public Reservation getReservation(String reservationId) throws NotFoundException {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("No se encontró la reserva con el ID: " + reservationId));
    }

}
