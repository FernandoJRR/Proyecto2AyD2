package com.ayd.reservation_service.qr.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.zxing.WriterException;

@ExtendWith(MockitoExtension.class)
public class QrCodeAdapterTest {

    private QrCodeAdapter qrCodeAdapter;

    @BeforeEach
    void setUp() {
        qrCodeAdapter = new QrCodeAdapter();
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
}
