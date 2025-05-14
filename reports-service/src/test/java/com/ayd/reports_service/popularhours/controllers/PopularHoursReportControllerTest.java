package com.ayd.reports_service.popularhours.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationTimeStatsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PopularHoursReportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PopularHoursReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReportServicePort<PopularHoursReportDTO, PeriodRequestDTO> reservationReportPort;

    private static final String BASE_URL = "/api/v1/popular-hours-report";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 11);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(11, 0);
    private static final Long TOTAL = 5l;

    private PeriodRequestDTO filters;
    private PopularHoursReportDTO reportDTO;

    @BeforeEach
    void setUp() {
        filters = new PeriodRequestDTO(DATE, DATE);
        ReservationTimeStatsDTO stat = new ReservationTimeStatsDTO(START_TIME, END_TIME, TOTAL);
        reportDTO = new PopularHoursReportDTO(List.of(stat), TOTAL.intValue());
    }

    /**
     * dado: request válido
     * cuando: se llama getPopularHoursBetweenDates
     * entonces: retorna DTO con status 200
     */
    @Test
    void getPopularHoursBetweenDatesReturnsDTO() throws Exception {
        when(reservationReportPort.generateReport(any())).thenReturn(reportDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.popularHours").isArray())
                .andExpect(jsonPath("$.totalReservations").value(TOTAL));
    }

    /**
     * dado: request válido
     * cuando: se llama exportPopularHoursBetweenDates
     * entonces: retorna PDF con headers y status 200
     */
    @Test
    void exportPopularHoursBetweenDatesReturnsPdfBytes() throws Exception {
        byte[] pdfBytes = new byte[] { 1, 2, 3 };
        when(reservationReportPort.generateReportAsPdf(any())).thenReturn(pdfBytes);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(content().bytes(pdfBytes));
    }

    /**
     * dado: error en exportación
     * cuando: se llama exportPopularHoursBetweenDates
     * entonces: retorna status 500
     */
    @Test
    void exportPopularHoursBetweenDatesThrowsError() throws Exception {
        when(reservationReportPort.generateReportAsPdf(any()))
                .thenThrow(new ReportGenerationExeption("Error"));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)))
                .andExpect(status().isInternalServerError());
    }
}
