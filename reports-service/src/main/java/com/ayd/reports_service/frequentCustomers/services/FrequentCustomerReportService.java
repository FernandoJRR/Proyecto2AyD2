package com.ayd.reports_service.frequentCustomers.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.frequentCustomers.dtos.FrequentCustomerDTO;
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
public class FrequentCustomerReportService implements ReportServicePort<List<FrequentCustomerDTO>, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final ReportParameterBuilderPort<List<FrequentCustomerDTO>> reportParameterBuilderPort;

    @Override
    public List<FrequentCustomerDTO> generateReport(PeriodRequestDTO filters) {
        // mandamos a a traer la info al serviio de reservaciones
        List<ReservationResponseDTO> reservations = reservationClientPort.getReservationReportByPeriod(filters);
        // fitramos solo las que tienen un nit asignado y agrupamos por el nit
        Map<String, List<ReservationResponseDTO>> groupedByCustomer = reservations.stream()
                .filter(r -> r.getCustomerNIT() != null && !r.getCustomerNIT().isEmpty())
                .collect(Collectors.groupingBy(reservation -> reservation.getCustomerNIT()));
        // construimos la lista de dtos
        List<FrequentCustomerDTO> result = groupedByCustomer.entrySet().stream()
                .map(entry -> {
                    String customerNIT = entry.getKey();
                    List<ReservationResponseDTO> customerReservations = entry.getValue();
                    String customerName = customerReservations.get(0).getCustomerFullName();
                    return new FrequentCustomerDTO(customerNIT, customerName, customerReservations.size());
                })
                .sorted(Comparator.comparing(FrequentCustomerDTO::getTotalVisits).reversed())
                .toList();
        return result;
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // mandar a construir el reporte
        List<FrequentCustomerDTO> report = generateReport(filters);
        // extraer los parametros del reporte
        reportParameterBuilderPort.init(report);
        Map<String, Object> params = reportParameterBuilderPort.buildParameters();
        return pdfPrinterPort.exportPdf("/FrequentCustomers", params);
    }

}
