package com.ayd.reports_service.reservationExports.ports;

import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

public interface ForReservationExportsPort {

    public byte[] exportReservationTicket(ReservationResponseDTO reservationResponseDTO) throws ReportGenerationExeption;

    public byte[] exportInvoiceWithQR(ReservationInterServiceDTO interServiceDTO) throws ReportGenerationExeption;
}
