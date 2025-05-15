package com.ayd.reports_service.averangeTime.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.averangeTime.dtos.AverangePerDate;
import com.ayd.reports_service.averangeTime.dtos.AverangeTimeReportDTO;
import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AverangeTimeReportService implements ReportServicePort<AverangeTimeReportDTO, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final ReportParameterBuilderPort<AverangeTimeReportDTO> reportParameterBuilderPort;

    @Override
    public AverangeTimeReportDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a traer la info del reporte
        List<ReservationResponseDTO> reservations = reservationClientPort
                .getReservationReportByPeriod(filters);

        // agrupar por fecha
        Map<LocalDate, List<ReservationResponseDTO>> groupedByDate = reservations.stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getDate()));

        List<AverangePerDate> averages = groupedByDate.entrySet().stream()
                .map(entry -> {
                    BigDecimal avgHours = averageHours(entry.getValue());
                    return new AverangePerDate(entry.getKey(), avgHours);
                }).toList();

        // calcular el average total
        BigDecimal globalAvg = averageHours(reservations);
        // retornamos el reporte
        return new AverangeTimeReportDTO(globalAvg, averages);
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // obtenemos el reporte
        AverangeTimeReportDTO report = generateReport(filters);
        // mandamos a extreaer la info para el reporte
        reportParameterBuilderPort.init(report);
        Map<String, Object> parameters = reportParameterBuilderPort.buildParameters();
        // mandamos a convertir a pdf
        return pdfPrinterPort.exportPdf("/AverageReport", parameters);
    }

    private BigDecimal averageHours(List<ReservationResponseDTO> reservations) {
        if (reservations.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalHours = reservations.stream()
                .map(r -> {
                    long minutes = Duration.between(r.getStartTime(), r.getEndTime()).toMinutes();
                    return BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalHours.divide(BigDecimal.valueOf(reservations.size()));
    }

}
