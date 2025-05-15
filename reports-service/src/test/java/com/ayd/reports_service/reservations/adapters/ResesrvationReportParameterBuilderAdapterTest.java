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

    @BeforeEach
    void setUp() {
        builder = new ResesrvationReportParameterBuilderAdapter();
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
