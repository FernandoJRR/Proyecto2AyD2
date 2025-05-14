package com.ayd.reports_service.reservations.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ReservationReportController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean
    private ReportServicePort<ReportReservationsDTO,PeriodRequestDTO> reservationReportPort;

    private final String BASE_URL = "/api/v1/reservation-reports";

    private final PeriodRequestDTO filters = new PeriodRequestDTO(LocalDate.now(), LocalDate.now());
    public static final String RESERVATION_ID = "R001";
    public static final LocalTime START_TIME = LocalTime.of(10, 0);
    public static final LocalTime END_TIME = LocalTime.of(11, 0);
    public static final LocalDate DATE = LocalDate.of(2025, 5, 11);
    public static final String USER_ID = "U123";
    public static final boolean ONLINE = true;
    public static final boolean PAID = true;
    public static final boolean CANCELLED = false;

    private ReservationResponseDTO reservation;
    private ReportReservationsDTO reportDTO;



    @BeforeEach
    void setUp() {
        reservation = new ReservationResponseDTO(
                RESERVATION_ID,
                START_TIME,
                END_TIME,
                DATE,
                USER_ID,
                ONLINE,
                PAID,
                CANCELLED);
        reportDTO = new ReportReservationsDTO(List.of(reservation), 1);
    }


    /**
     * dado: request válido
     * cuando: se llama createReservationReport
     * entonces: retorna DTO con status 200
     */
    @Test
    void createReservationReportReturnsDTO() throws Exception {
        when(reservationReportPort.generateReport(any())).thenReturn(reportDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservations").isArray())
                .andExpect(jsonPath("$.totalReservations").value(1));
    }

    /**
     * dado: request válido
     * cuando: se llama exportReservationReport
     * entonces: retorna PDF con headers y status 200
     */
    @Test
    void exportReservationReportReturnsPdfBytes() throws Exception {
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
     * cuando: se llama exportReservationReport
     * entonces: retorna status 500
     */
    @Test
    void exportReservationReportThrowsError() throws Exception {
        when(reservationReportPort.generateReportAsPdf(any())).thenThrow(new ReportGenerationExeption("Error"));

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)))
                .andExpect(status().isInternalServerError());
    }
}
