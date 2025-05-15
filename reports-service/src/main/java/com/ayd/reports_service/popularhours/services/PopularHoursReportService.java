package com.ayd.reports_service.popularhours.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopularHoursReportService implements ReportServicePort<PopularHoursReportDTO, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final ReportParameterBuilderPort<PopularHoursReportDTO> reportParameterBuilderPort;

    @Override
    public PopularHoursReportDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a traer la info del reporte
        List<ReservationTimeStatsDTO> popularHours = reservationClientPort
                .getPopularHoursBetweenDates(filters);
        // mandamos a calcular el total de reservas
        Integer totalReservations = getTotalReservations(popularHours);
        // retornamos el reporte
        return new PopularHoursReportDTO(popularHours, totalReservations);
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // obtenemos el reporte
        PopularHoursReportDTO report = generateReport(filters);
        // mandamos a extreaer la info para el reporte
        reportParameterBuilderPort.init(report);
        Map<String, Object> parameters = reportParameterBuilderPort.buildParameters();
        // mandamos a convertir a pdf
        return pdfPrinterPort.exportPdf("/PopularHours", parameters);
    }

    private Integer getTotalReservations(List<ReservationTimeStatsDTO> popularHours) {
        Integer total = 0;
        for (ReservationTimeStatsDTO hour : popularHours) {
            total += hour.getTotal().intValue();
        }
        return total;
    }

}
