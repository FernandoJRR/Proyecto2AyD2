package com.ayd.reports_service.reservations.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.reservations.port.ReportParameterBuilderPort;
import com.ayd.reports_service.reservations.port.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

@ExtendWith(MockitoExtension.class)
class ReservationReportServiceTest {

    private static final PeriodRequestDTO PERIOD_DTO = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());
    private static final byte[] PDF_BYTES = new byte[] { 1, 2, 3 };

    public static final String RESERVATION_ID = "R001";
    public static final LocalTime START_TIME = LocalTime.of(10, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 0);
    public static final LocalDate DATE = LocalDate.of(2025, 5, 11);
    public static final String USER_ID = "U123";
    public static final boolean ONLINE = true;
    public static final boolean PAID = true;
    public static final boolean CANCELLED = false;

    public static final String RESERVATION_ID_2 = "R002";
    public static final LocalTime START_TIME_2 = LocalTime.of(14, 30);
    public static final LocalTime END_TIME_2 = LocalTime.of(15, 30);
    public static final LocalDate DATE_2 = LocalDate.of(2025, 5, 12);
    public static final String USER_ID_2 = "U456";
    public static final boolean ONLINE_2 = false;
    public static final boolean PAID_2 = false;
    public static final boolean CANCELLED_2 = true;

    private ReservationResponseDTO response1;
    private ReservationResponseDTO response2;

    @Mock
    private PdfPrinterPort pdfPrinterPort;
    @Mock
    private ReservationClientPort reservationClientPort;
    @Mock
    private ReportParameterBuilderPort<ReportReservationsDTO> reportParameterBuilderPort;

    @InjectMocks
    private ReservationReportService reservationReportService;

    @BeforeEach
    void setUp() {
        response1 = new ReservationResponseDTO(
                RESERVATION_ID,
                START_TIME,
                END_TIME,
                DATE,
                USER_ID,
                ONLINE,
                PAID,
                CANCELLED);

        response2 = new ReservationResponseDTO(
                RESERVATION_ID_2,
                START_TIME_2,
                END_TIME_2,
                DATE_2,
                USER_ID_2,
                ONLINE_2,
                PAID_2,
                CANCELLED_2);
    }

    /**
     * dado: filtro de periodo
     * cuando: se llama generateReport
     * entonces: retorna dto con lista y total de reservas
     */
    @Test
    void generateReportReturnsCorrectDTO() {
        // arrange
        List<ReservationResponseDTO> reservations = List.of(response1, response2);
        when(reservationClientPort.getReservationReportByPeriod(PERIOD_DTO)).thenReturn(reservations);

        // act
        ReportReservationsDTO result = reservationReportService.generateReport(PERIOD_DTO);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalReservations()),
                () -> assertEquals(reservations, result.getReservations()));
    }

    /**
     * dado: filtro de periodo
     * cuando: se llama generateReportAsPdf
     * entonces: retorna pdf no vacio
     */
    @Test
    void generateReportAsPdfReturnsPdfBytes() throws Exception {
        // arrange
        List<ReservationResponseDTO> reservations = List.of(response1, response2);
        when(reservationClientPort.getReservationReportByPeriod(PERIOD_DTO)).thenReturn(reservations);
        when(pdfPrinterPort.exportPdf(anyString(), anyMap())).thenReturn(PDF_BYTES);

        // act
        byte[] pdf = reservationReportService.generateReportAsPdf(PERIOD_DTO);

        // assert
        assertAll(
                () -> assertNotNull(pdf),
                () -> assertEquals(PDF_BYTES.length, pdf.length),
                () -> assertArrayEquals(PDF_BYTES, pdf));
    }

    /**
     * dado: error en exportPdf
     * cuando: se llama generateReportAsPdf
     * entonces: lanza ReportGenerationExeption
     */
    @Test
    void generateReportAsPdfThrowsException() throws Exception {
        // arrange
        when(reservationClientPort.getReservationReportByPeriod(PERIOD_DTO)).thenReturn(Collections.emptyList());
        when(pdfPrinterPort.exportPdf(anyString(), anyMap())).thenThrow(new ReportGenerationExeption("Error"));

        // act y assert
        assertThrows(ReportGenerationExeption.class, () -> reservationReportService.generateReportAsPdf(PERIOD_DTO));
    }
}
