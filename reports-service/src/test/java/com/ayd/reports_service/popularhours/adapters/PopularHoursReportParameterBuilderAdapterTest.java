package com.ayd.reports_service.popularhours.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

class PopularHoursReportParameterBuilderAdapterTest {

    private PopularHoursReportParameterBuilderAdapter builder;
    private PopularHoursReportDTO reportDTO;

    private static final LocalTime START_TIME_1 = LocalTime.of(10, 0);
    private static final LocalTime END_TIME_1 = LocalTime.of(11, 0);
    private static final Long RES_COUNT_1 = 5l;

    private static final LocalTime START_TIME_2 = LocalTime.of(15, 0);
    private static final LocalTime END_TIME_2 = LocalTime.of(16, 0);
    private static final Long RES_COUNT_2 = 8l;

    @BeforeEach
    void setUp() {
        builder = new PopularHoursReportParameterBuilderAdapter();
        ReservationTimeStatsDTO row1 = new ReservationTimeStatsDTO(START_TIME_1, END_TIME_1, RES_COUNT_1);
        ReservationTimeStatsDTO row2 = new ReservationTimeStatsDTO(START_TIME_2, END_TIME_2, RES_COUNT_2);
        reportDTO = new PopularHoursReportDTO(List.of(row1, row2), (RES_COUNT_1.intValue() + RES_COUNT_2.intValue()));
    }

    /**
     * dado: DTO de reporte válido
     * cuando: se llama init y luego buildParameters
     * entonces: retorna mapa con claves esperadas y tipos correctos
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
                () -> assertEquals(RES_COUNT_1.intValue() + RES_COUNT_2.intValue(), params.get("totalReservations")));
    }
}
