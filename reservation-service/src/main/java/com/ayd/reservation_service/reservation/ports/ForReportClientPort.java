package com.ayd.reservation_service.reservation.ports;

import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

public interface ForReportClientPort {
    public byte[] exportInvoiceWithQR(ReservationInterServiceDTO reservationInterServiceDTO);

    public byte[] exportReservationTicket(ReservationResponseDTO reservationInterServiceDTO);
}
