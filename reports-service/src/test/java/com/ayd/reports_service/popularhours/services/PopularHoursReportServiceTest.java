package com.ayd.reports_service.popularhours.services;

import static org.junit.jupiter.api.Assertions.*;
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
import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.reports_service.shared.ports.ReservationClientPort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

@ExtendWith(MockitoExtension.class)
class PopularHoursReportServiceTest {

    @Mock
    private PdfPrinterPort pdfPrinterPort;

    @Mock
    private ReservationClientPort reservationClientPort;

    @Mock
    private ReportParameterBuilderPort<PopularHoursReportDTO> reportParameterBuilderPort;

    @InjectMocks
    private PopularHoursReportService reportService;

    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(10, 0);
    private static final Long TOTAL = 3l;
    private static final LocalDate DATE = LocalDate.of(2025, 5, 10);
    private static final PeriodRequestDTO FILTERS = new PeriodRequestDTO(DATE, DATE);
    private static final byte[] PDF_BYTES = new byte[]{1, 2, 3};

    private ReservationTimeStatsDTO stat;

    @BeforeEach
    void setUp() {
        stat = new ReservationTimeStatsDTO(START_TIME, END_TIME, TOTAL);
    }

    /**
     * dado: lista con horas populares
     * cuando: se llama generateReport
     * entonces: retorna DTO con lista y total
     */
    @Test
    void generateReportReturnsDtoWithData() {
        // arrange
        when(reservationClientPort.getPopularHoursBetweenDates(FILTERS))
                .thenReturn(List.of(stat));

        // act
        PopularHoursReportDTO result = reportService.generateReport(FILTERS);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getPopularHours().size()),
                () -> assertEquals(TOTAL.intValue(), result.getTotalReservations())
        );
    }

    /**
     * dado: filtros válidos y dependencias mockeadas
     * cuando: se llama generateReportAsPdf
     * entonces: retorna byte[] de PDF
     */
    @Test
    void generateReportAsPdfReturnsPdfBytes() throws ReportGenerationExeption {
        // arrange
        when(reservationClientPort.getPopularHoursBetweenDates(FILTERS))
                .thenReturn(List.of(stat));
        when(reportParameterBuilderPort.buildParameters()).thenReturn(Map.of("key", "value"));
        when(pdfPrinterPort.exportPdf(any(), any())).thenReturn(PDF_BYTES);

        // act
        byte[] result = reportService.generateReportAsPdf(FILTERS);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PDF_BYTES.length, result.length),
                () -> verify(reportParameterBuilderPort).init(any()),
                () -> verify(reportParameterBuilderPort).buildParameters(),
                () -> verify(pdfPrinterPort).exportPdf(any(), any())
        );
    }

    /**
     * dado: una hora con total null
     * cuando: se llama generateReport
     * entonces: lanza NullPointerException
     */
    @Test
    void generateReportFailsIfTotalIsNull() {
        // arrange
        ReservationTimeStatsDTO invalidStat = new ReservationTimeStatsDTO(START_TIME, END_TIME, null);
        when(reservationClientPort.getPopularHoursBetweenDates(FILTERS))
                .thenReturn(List.of(invalidStat));

        // act & assert
        assertThrows(NullPointerException.class, () -> reportService.generateReport(FILTERS));
    }
}
