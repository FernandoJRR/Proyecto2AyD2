package com.ayd.reports_service.reservationProfit.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservationProfit.dtos.ReservationProfitDTO;
import com.ayd.reports_service.shared.ports.InvoiceClientPort;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationProfitReportService implements ReportServicePort<ReservationProfitDTO, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final InvoiceClientPort invoiceClientPort;

    @Override
    public ReservationProfitDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a buscar las reservas hechas en el filtro
        List<ReservationResponseDTO> reservations = reservationClientPort.getReservationReportByPeriod(filters);
        // extraemos todos los ids de las facturas de las reservaciones, unicamente las
        // marcadas como pagadas
        List<String> invoiceIds = reservations.stream().filter(reservation -> reservation.getPaid())
                .map(reservation -> reservation.getInvoiceId())
                .filter(invoiceId -> invoiceId != null).collect(Collectors.toList());
        // obtenemos las invoices por sus ids
        throw new UnsupportedOperationException("Unimplemented method 'generateReport'");
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateReportAsPdf'");
    }

}
