package com.ayd.reservation_service.reservation.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.reservation_service.reservation.models.Reservation;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {



    public ReservationResponseDTO fromReservationToReservationResponseDTO(
            Reservation reservation);

    public List<ReservationResponseDTO> fromReservationsToReservationResponseDTOs(
            List<Reservation> reservations);
}
