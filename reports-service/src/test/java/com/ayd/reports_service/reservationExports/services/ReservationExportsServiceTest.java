package com.ayd.reports_service.reservationExports.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedInvoiceService.dtos.InvoiceDetailResponseDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;
import com.ayd.sharedInvoiceService.enums.ItemType;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import com.ayd.sharedReservationService.dto.ReservationInterServiceDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class ReservationExportsServiceTest {

    private static final String RES_ID = "res-001";
    private static final String INV_ID = "inv-001";
    private static final String CLIENT_NAME = "Pedro Gomez";
    private static final String CLIENT_NIT = "12345678-9";
    private static final String GAME_ID = "game-123";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 15);
    private static final LocalTime START = LocalTime.of(10, 0);
    private static final LocalTime END = LocalTime.of(11, 0);
    private static final byte[] PDF_BYTES = new byte[] { 1, 2, 3 };

    @Mock
    private PdfPrinterPort pdfPrinterPort;

    @Mock
    private ReportParameterBuilderPort<ReservationInterServiceDTO> invoiceQrParameterBuilder;

    @Mock
    private ReportParameterBuilderPort<ReservationResponseDTO> reservationTicketParameterBuilder;

    @InjectMocks
    private ReservationExportsService service;

    private ReservationInterServiceDTO interServiceDTO;
    private ReservationResponseDTO reservationDTO;

    @BeforeEach
    void setUp() {
        InvoiceDetailResponseDTO detail = new InvoiceDetailResponseDTO(
                "detail-001", "item-001", "Pizza", ItemType.GOOD, 1,
                new BigDecimal("100.00"), new BigDecimal("100.00"));

        InvoiceResponseDTO invoice = new InvoiceResponseDTO(
                INV_ID, PaymentMethod.CARD, new BigDecimal("100.00"),
                new BigDecimal("12.00"), new BigDecimal("112.00"),
                CLIENT_NIT, List.of(detail));

        reservationDTO = new ReservationResponseDTO(
                RES_ID, START, END, DATE, false, true,
                GAME_ID, CLIENT_NAME, CLIENT_NIT, INV_ID);

        interServiceDTO = new ReservationInterServiceDTO(reservationDTO, invoice);
    }

    /**
     * dado: DTO válido para invoice con QR
     * cuando: se llama exportInvoiceWithQR
     * entonces: retorna byte[] con contenido PDF
     */
    @Test
    void exportInvoiceWithQRReturnsPdfBytes() throws ReportGenerationExeption {
        // arrange
  
        when(pdfPrinterPort.exportPdf(any(), any())).thenReturn(PDF_BYTES);

        // act
        byte[] result = service.exportInvoiceWithQR(interServiceDTO);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PDF_BYTES.length, result.length));
    }

    /**
     * dado: DTO válido de reservación
     * cuando: se llama exportReservationTicket
     * entonces: retorna byte[] con contenido PDF
     */
    @Test
    void exportReservationTicketReturnsPdfBytes() throws ReportGenerationExeption {
        // arrange
        when(reservationTicketParameterBuilder.buildParameters()).thenReturn(Map.of("key2", "value2"));
        when(pdfPrinterPort.exportPdf(any(), any())).thenReturn(PDF_BYTES);

        // act
        byte[] result = service.exportReservationTicket(reservationDTO);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PDF_BYTES.length, result.length));
    }
}
