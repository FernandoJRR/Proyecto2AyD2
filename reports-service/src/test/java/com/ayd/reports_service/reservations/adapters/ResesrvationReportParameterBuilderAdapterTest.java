package com.ayd.reports_service.reservations.adapters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
public class ResesrvationReportParameterBuilderAdapterTest {

    private ResesrvationReportParameterBuilderAdapter builder;
    private ReportReservationsDTO reportDTO;

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

    @BeforeEach
    void setUp() {
        builder = new ResesrvationReportParameterBuilderAdapter();
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

        reportDTO = new ReportReservationsDTO(List.of(response1, response2), 2);
    }

    /**
     * dado: dto de reporte válido
     * cuando: se llama init y luego buildParameters
     * entonces: retorna mapa con claves esperadas y tipo correcto de DataSource
     */
    @Test
    void buildParametersReturnsValidMap() {
        // arrange
        builder.init(reportDTO);

        // act
        Map<String, Object> params = builder.buildParameters();

        // assert
        assertAll(
                () -> assertNotNull(params),
                () -> assertEquals(2, params.size()),
                () -> assertTrue(params.containsKey("reservations")),
                () -> assertTrue(params.containsKey("totalReservations")),
                () -> assertTrue(params.get("reservations") instanceof JRBeanArrayDataSource),
                () -> assertEquals(2, params.get("totalReservations")));
    }
}
