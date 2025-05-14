package com.ayd.reports_service.qr.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.shared.security.AppProperties;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;
import com.google.zxing.WriterException;

@ExtendWith(MockitoExtension.class)
public class QrCodeAdapterTest {

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private QrCodeAdapter qrCodeAdapter;

    private static final String TEST_GAME_ID = "abc123";
    private static final String TEST_FRONT_URL = "https://test.com";

    private static final ReservationResponseDTO TEST_RESERVATION = new ReservationResponseDTO(
            "",
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalDate.of(2025, 5, 14),
            true,
            true,
            TEST_GAME_ID,
            "",
            "");

    @BeforeEach
    void setUp() {

    }

    /**
     * dado: un texto válido.
     * cuando: se genera el código QR.
     * entonces: se retorna un arreglo de bytes PNG no vacío.
     */
    @Test
    void generateQrCodeReturnsNonEmptyByteArray() throws WriterException, IOException {
        // arrange
        String text = "xddddddd";

        // act
        byte[] result = qrCodeAdapter.generateQrCode(text);

        // assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.length > 0));
    }

    /**
     * dado: una reservación con gameId y un frontURL válido.
     * cuando: se llama createReservationQR.
     * entonces: se genera un arreglo de bytes de QR no vacío.
     */
    @Test
    void createReservationQRReturnsValidQrCode() throws WriterException, IOException {
        // arrange
        when(appProperties.getFrontURL()).thenReturn(TEST_FRONT_URL);

        // act
        byte[] qrCode = qrCodeAdapter.createReservationQR(TEST_RESERVATION);

        // assert
        assertAll(
                () -> assertNotNull(qrCode),
                () -> assertTrue(qrCode.length > 0));
    }
}