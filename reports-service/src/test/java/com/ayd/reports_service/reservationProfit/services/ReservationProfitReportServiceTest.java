package com.ayd.reports_service.reservationProfit.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservationProfit.dtos.ReservationProfitDTO;
import com.ayd.reports_service.shared.ports.InvoiceClientPort;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

@ExtendWith(MockitoExtension.class)
class ReservationProfitReportServiceTest {

    private static final String RES_ID = "resv-001";
    private static final String INV_ID = "inv-001";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 15);
    private static final LocalTime START = LocalTime.of(10, 0);
    private static final LocalTime END = LocalTime.of(11, 0);
    private static final String CLIENT_NAME = "Ana Torres";
    private static final String CLIENT_NIT = "1234567-8";
    private static final BigDecimal TOTAL = new BigDecimal("200.00");
    private static final byte[] PDF_BYTES = new byte[] { 1, 2, 3 };

    @Mock
    private PdfPrinterPort pdfPrinterPort;

    @Mock
    private ReservationClientPort reservationClientPort;

    @Mock
    private InvoiceClientPort invoiceClientPort;

    @Mock
    private ReportParameterBuilderPort<ReservationProfitDTO> parameterBuilderPort;

    @InjectMocks
    private ReservationProfitReportService reportService;

    private PeriodRequestDTO filters;
    private ReservationResponseDTO reservation;
    private InvoiceResponseDTO invoice;

    @BeforeEach
    void setUp() {
        filters = new PeriodRequestDTO(DATE, DATE);
        reservation = new ReservationResponseDTO(
                RES_ID, START, END, DATE, true, true, "game-001",
                CLIENT_NAME, CLIENT_NIT, INV_ID);
        invoice = new InvoiceResponseDTO(
                INV_ID, PaymentMethod.CASH, TOTAL, BigDecimal.ZERO, TOTAL, CLIENT_NIT, List.of());
    }

    /**
     * dado: filtro de fechas válido con una reservación pagada
     * cuando: se llama generateReport
     * entonces: retorna DTO con una reservación y total correcto
     */
    @Test
    void generateReportReturnsCorrectProfitDTO() {
        // arrange
        when(reservationClientPort.getReservationReportByPeriod(any())).thenReturn(List.of(reservation));
        when(invoiceClientPort.getProfitsByIds(any())).thenReturn(List.of(invoice));

        // act
        ReservationProfitDTO result = reportService.generateReport(filters);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(TOTAL, result.getTotalProfit()),
                () -> assertEquals(1, result.getReservations().size()));
    }

    /**
     * dado: filtros válidos y dependencias mockeadas
     * cuando: se llama generateReportAsPdf
     * entonces: retorna byte[] de PDF
     */
    @Test
    void generateReportAsPdfReturnsPdfBytes() throws ReportGenerationExeption {
        // arrange
        when(reservationClientPort.getReservationReportByPeriod(any())).thenReturn(List.of(reservation));
        when(invoiceClientPort.getProfitsByIds(any())).thenReturn(List.of(invoice));

        // construimos manualmente el resultado esperado del buildParameters
        Map<String, Object> expectedParams = Map.of("key", "value");

        // simulamos la construcción de parámetros y el retorno del PDF
        doNothing().when(parameterBuilderPort).init(any());
        when(parameterBuilderPort.buildParameters()).thenReturn(expectedParams);
        when(pdfPrinterPort.exportPdf(any(), eq(expectedParams))).thenReturn(PDF_BYTES);

        // act
        byte[] result = reportService.generateReportAsPdf(filters);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PDF_BYTES.length, result.length),
                () -> verify(parameterBuilderPort).init(any()),
                () -> verify(parameterBuilderPort).buildParameters(),
                () -> verify(pdfPrinterPort).exportPdf(any(), eq(expectedParams)));
    }

}
