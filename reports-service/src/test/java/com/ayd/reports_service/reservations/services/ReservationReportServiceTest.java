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
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

@ExtendWith(MockitoExtension.class)
class ReservationReportServiceTest {

    private static final PeriodRequestDTO PERIOD_DTO = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());
    private static final byte[] PDF_BYTES = new byte[] { 1, 2, 3 };

    public static final String RESERVATION_ID = "resv-123";
    public static final LocalTime START_TIME = LocalTime.of(10, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 0);
    public static final LocalDate DATE = LocalDate.of(2025, 5, 20);
    public static final boolean PAID = false;
    public static final boolean NOT_SHOW = true;
    public static final String GAME_ID = "game-456";
    public static final String CUSTOMER_NAME = "Juan Pérez";
    public static final String CUSTOMER_CUI = "1234567-8";

    public static final String RESERVATION_ID_2 = "resv-789";
    public static final LocalTime START_TIME_2 = LocalTime.of(14, 30);
    public static final LocalTime END_TIME_2 = LocalTime.of(15, 30);
    public static final LocalDate DATE_2 = LocalDate.of(2025, 6, 5);
    public static final boolean PAID_2 = true;
    public static final boolean NOT_SHOW_2 = false;
    public static final String GAME_ID_2 = "game-999";
    public static final String CUSTOMER_NAME_2 = "María Gómez";
    public static final String CUSTOMER_CUI_2 = "9876543-2";

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
                PAID,
                NOT_SHOW,
                GAME_ID,
                CUSTOMER_NAME,
                CUSTOMER_CUI,"");

        response2 = new ReservationResponseDTO(
                RESERVATION_ID_2,
                START_TIME_2,
                END_TIME_2,
                DATE_2,
                PAID_2,
                NOT_SHOW_2,
                GAME_ID_2,
                CUSTOMER_NAME_2,
                CUSTOMER_CUI_2,"");

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
