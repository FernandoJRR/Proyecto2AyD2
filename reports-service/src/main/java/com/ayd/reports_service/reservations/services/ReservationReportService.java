package com.ayd.reports_service.reservations.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.reservations.port.ReservationClientPort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationReportService implements ReportServicePort<ReportReservationsDTO, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final ReportParameterBuilderPort<ReportReservationsDTO> reportParameterBuilderPort;

    public ReportReservationsDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a a traer la info al serviio de reservaciones
        List<ReservationResponseDTO> reservations = reservationClientPort.getReservationReportByPeriod(filters);
        // calculamos el total de las reservas
        Integer totalReservations = reservations.size();
        // creamos el dto de del reporte
        return new ReportReservationsDTO(reservations, totalReservations);
    }

    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // mandar a construir el reporte
        ReportReservationsDTO report = generateReport(filters);
        // extraer los parametros del reporte
        reportParameterBuilderPort.init(report);
        Map<String, Object> params = reportParameterBuilderPort.buildParameters();
        return pdfPrinterPort.exportPdf("/ReservationsReport", params);
    }

}
