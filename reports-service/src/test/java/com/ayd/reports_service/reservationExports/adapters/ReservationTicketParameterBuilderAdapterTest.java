package com.ayd.reports_service.reservationExports.adapters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

@ExtendWith(MockitoExtension.class)
public class ReservationTicketParameterBuilderAdapterTest {

    private static final String RESERVATION_ID = "resv-123";
    private static final String CLIENT_NIT = "1234567-8";
    private static final String CLIENT_NAME = "Juan Pérez";
    private static final LocalDate DATE = LocalDate.of(2025, 5, 20);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalTime END_TIME = LocalTime.of(11, 0);
    private static final String GAME_ID = "game-456";
    private static final String INVOICE_ID = "inv-001";

    private ReservationResponseDTO reservation;

    @InjectMocks
    private ReservationTicketParameterBuilderAdapter builder;

    @BeforeEach
    void setUp() {
        reservation = new ReservationResponseDTO(
                RESERVATION_ID,
                START_TIME,
                END_TIME,
                DATE,
                false,
                true,
                GAME_ID,
                CLIENT_NAME,
                CLIENT_NIT,
                INVOICE_ID);
        builder.init(reservation);
    }

    /**
     * dado: dto válido
     * cuando: se llama buildParameters
     * entonces: retorna mapa con claves esperadas y valores correctos
     */
    @Test
    void buildParametersReturnsValidMap() {
        // act
        Map<String, Object> params = builder.buildParameters();

        // assert
        assertAll(
                () -> assertNotNull(params),
                () -> assertEquals(6, params.size()),
                () -> assertEquals(RESERVATION_ID, params.get("reservationId")),
                () -> assertEquals(CLIENT_NIT, params.get("clientNit")),
                () -> assertEquals(CLIENT_NAME, params.get("clientFullname")),
                () -> assertEquals(DATE, params.get("date")),
                () -> assertEquals(START_TIME, params.get("startTime")),
                () -> assertEquals(END_TIME, params.get("endTime")));
    }
}
