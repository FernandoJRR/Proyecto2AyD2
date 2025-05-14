package com.ayd.reservation_service.reservation.ports;

import java.io.IOException;
import java.util.List;

import com.ayd.reservation_service.reservation.dtos.CreateReservationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedReservationService.dto.ReservationSpecificationRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;
import com.google.zxing.WriterException;

public interface ForReservationPort {

    List<Reservation> getReservationsBetweenDates(PeriodRequestDTO periodRequestDTO);

    public Reservation getReservation(String reservationId) throws NotFoundException;

    public byte[] createPresentialReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException, WriterException, IOException;

    public Reservation createOnlineReservation(CreateReservationRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException;

    public Reservation cancelReservation(String reservationId) throws IllegalStateException, NotFoundException;

    public Reservation setPaymentReservation(String reservationId) throws IllegalStateException, NotFoundException;

    public boolean deleteReservation(String reservationId) throws IllegalStateException, NotFoundException;

    public List<Reservation> getReservations(ReservationSpecificationRequestDTO reservationSpecificationRequestDTO);

    public List<ReservationTimeStatsDTO> getPopularHoursBetweenDates(PeriodRequestDTO periodRequestDTO);
}
