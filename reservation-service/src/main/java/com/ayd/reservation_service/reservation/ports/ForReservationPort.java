package com.ayd.reservation_service.reservation.ports;

import java.io.IOException;
import java.util.List;

import com.ayd.reservation_service.reservation.dtos.CreateReservationOnlineRequestDTO;
import com.ayd.reservation_service.reservation.dtos.CreateReservationPresentialRequestDTO;
import com.ayd.reservation_service.reservation.dtos.PayReservationRequestDTO;
import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedReservationService.dto.ReservationSpecificationRequestDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;
import com.google.zxing.WriterException;

public interface ForReservationPort {

    public byte[] createPresentialReservation(CreateReservationPresentialRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException, WriterException, IOException;

    public byte[] payReservation(PayReservationRequestDTO payReservationRequestDTO)
            throws NotFoundException, WriterException, IOException;

    public byte[] getReservationInvoice(String reservationId) throws WriterException, IOException, NotFoundException;

    public byte[] createOnlineReservation(CreateReservationOnlineRequestDTO createReservationRequestDTO)
            throws DuplicatedEntryException;

    public Reservation getReservation(String reservationId) throws NotFoundException;

    public Reservation cancelReservation(String reservationId) throws IllegalStateException, NotFoundException;

    public boolean deleteReservation(String reservationId) throws IllegalStateException, NotFoundException;

    public List<Reservation> getReservations(ReservationSpecificationRequestDTO reservationSpecificationRequestDTO);

    public List<ReservationTimeStatsDTO> getPopularHoursBetweenDates(PeriodRequestDTO periodRequestDTO);

    public List<Reservation> getReservationsBetweenDates(PeriodRequestDTO periodRequestDTO);

}
