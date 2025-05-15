package com.ayd.reports_service.noShows.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.noShows.ports.NotShowReportPort;
import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service("notShowReportService")
@RequiredArgsConstructor
public class NotShowReportService implements NotShowReportPort {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final ReportParameterBuilderPort<ReportReservationsDTO> reportParameterBuilderPort;

    @Override
    public ReportReservationsDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a a traer la info al serviio de reservaciones
        List<ReservationResponseDTO> reservations = reservationClientPort.getReservationReportByPeriod(filters);
        // filtramos solo los notshow
        reservations = reservations.stream().filter(reservation -> reservation.getNotShow()).toList();
        // calculamos el total de las reservas
        Integer totalReservations = reservations.size();
        // creamos el dto de del reporte
        return new ReportReservationsDTO(reservations, totalReservations);
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // mandar a construir el reporte
        ReportReservationsDTO report = generateReport(filters);
        // extraer los parametros del reporte
        reportParameterBuilderPort.init(report);
        Map<String, Object> params = reportParameterBuilderPort.buildParameters();
        return pdfPrinterPort.exportPdf("/NotShowReport", params);
    }

}
