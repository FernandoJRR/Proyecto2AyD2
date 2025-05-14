package com.ayd.reports_service.reservationExports.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservationExports.ports.ForReservationExportsPort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationExportsService implements ForReservationExportsPort {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReportParameterBuilderPort<ReservationInterServiceDTO> invoiceQrParameterBuilder;
    private final ReportParameterBuilderPort<ReservationResponseDTO> reservationTicketParameterBuilder;


    @Override
    public byte[] exportInvoiceWithQR(ReservationInterServiceDTO interServiceDTO) throws ReportGenerationExeption {
        // mandamos a crear los parametros para la invoice con qr
        invoiceQrParameterBuilder.init(interServiceDTO);
        Map<String, Object> parameters = invoiceQrParameterBuilder.buildParameters();
        // mandamos a exportar el reporte a pdf
        byte[] report = pdfPrinterPort.exportPdf("/InvoiceQr", parameters);
        return report;
    }
 
    @Override
    public byte[] exportReservationTicket(ReservationResponseDTO reservationResponseDTO) throws ReportGenerationExeption {
        // mandamos a crear los parametros para la invoice con qr
        reservationTicketParameterBuilder.init(reservationResponseDTO);
        Map<String, Object> parameters = invoiceQrParameterBuilder.buildParameters();
        // mandamos a exportar el reporte a pdf
        byte[] report = pdfPrinterPort.exportPdf("/ReservationTicket", parameters);

        return report;
    }

}
