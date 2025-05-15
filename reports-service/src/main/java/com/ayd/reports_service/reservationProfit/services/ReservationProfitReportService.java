package com.ayd.reports_service.reservationProfit.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservationProfit.dtos.ReservationProfitDTO;
import com.ayd.reports_service.reservationProfit.dtos.ReservationWithPayMethod;
import com.ayd.reports_service.shared.ports.InvoiceClientPort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationProfitReportService implements ReportServicePort<ReservationProfitDTO, PeriodRequestDTO> {

    private final PdfPrinterPort pdfPrinterPort;
    private final ReservationClientPort reservationClientPort;
    private final InvoiceClientPort invoiceClientPort;
    private final ReportParameterBuilderPort<ReservationProfitDTO> parameterBuilderPort;

    @Override
    public ReservationProfitDTO generateReport(PeriodRequestDTO filters) {
        // mandamos a buscar las reservas hechas en el filtro
        List<ReservationResponseDTO> reservations = reservationClientPort.getReservationReportByPeriod(filters);

        // filtramos las pagadas
        reservations = reservations.stream().filter(reservation -> reservation.getPaid()).toList();

        // extraemos todos los ids de las facturas de las reservaciones, unicamente las
        // marcadas como pagadas
        List<String> invoiceIds = reservations.stream()
                .map(reservation -> reservation.getInvoiceId())
                .filter(invoiceId -> invoiceId != null).toList();

        // obtenemos las invoices por sus ids
        List<InvoiceResponseDTO> invoices = invoiceClientPort.getProfitsByIds(invoiceIds);

        // ahora construimos la lista de reservaciones con el fomrato del reporte

        List<ReservationWithPayMethod> result = reservations.stream()
                .map(reservation -> {

                    // mandamos a filtrar las invoices que coincidad en su id con el guardado en la
                    // reservacio nactual
                    InvoiceResponseDTO invoice = invoices.stream()
                            .filter(invoiceFilter -> invoiceFilter.getId().equals(reservation.getInvoiceId()))
                            .findFirst()
                            .orElse(null);

                    // inicialiamos los datos de la invoice por si acaso esta nula
                    BigDecimal total = BigDecimal.ZERO;
                    PaymentMethod method = PaymentMethod.CARD;
                    if (invoice != null) {
                        total = invoice.getTotal();
                        method = invoice.getPaymentMethod();
                    }
                    // construimos el objeto con la informacion de la invoice encontrada
                    // y con la informacion de la reservacion en la iteracion actual
                    return new ReservationWithPayMethod(
                            reservation.getId(),
                            reservation.getDate(),
                            reservation.getStartTime(),
                            reservation.getEndTime(),
                            reservation.getCustomerFullName(),
                            reservation.getCustomerNIT(),
                            method.toString(),
                            total);
                })
                .toList();

        // sumamos el total de las invoices
        BigDecimal total = invoices.stream().map(invoice -> invoice.getTotal()).reduce(BigDecimal.ZERO,
                (acummulator, actualNumber) -> {
                    return acummulator.add(actualNumber);
                });
        // ceramos la respuesata
        ReservationProfitDTO dto = new ReservationProfitDTO(total, result);
        return dto;
    }

    @Override
    public byte[] generateReportAsPdf(PeriodRequestDTO filters) throws ReportGenerationExeption {
        // creamos el reporte
        ReservationProfitDTO report = generateReport(filters);
        // construimos los parametros
        parameterBuilderPort.init(report);
        Map<String, Object> paramsMap = parameterBuilderPort.buildParameters();
        // mandamos a crear los parametros
        return pdfPrinterPort.exportPdf("/ReservationsProfit", paramsMap);
    }

}
