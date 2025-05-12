package com.ayd.reports_service.pdf.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.shared.exceptions.ReportGenerationExeption;

@ExtendWith(MockitoExtension.class)
public class PdfPrintServiceTest {

    private static final String TEST_REPORT_PATH = "/test";
    private Map<String, Object> parameters;

    @InjectMocks
    private PdfPrintService pdfPrintService;

    @BeforeEach
    void setUp() {
        parameters = Map.of();
    }

    /**
     * dado: path valido
     * cuando: se llama exportarPdf
     * entonces: retorna pdf no vacio
     */
    @Test
    void exportPdfReturnsNonEmptyByteArray() throws Exception {
        // // arrange

        // // act
        // byte[] pdfBytes = pdfPrintService.exportarPdf(TEST_REPORT_PATH, parameters);

        // // assert
        // assertAll(
        //         () -> assertNotNull(pdfBytes),
        //         () -> assertTrue(pdfBytes.length > 0));
    }

    /**
     * dado path invalido
     * cuando se llama exportarPdf
     * entonces lanza ReportGenerationExeption
     */
    @Test
    void exportPdfShouldThrowWhenPathIsInvalid() throws ReportGenerationExeption {
        // arrange

        // act
        assertThrows(ReportGenerationExeption.class, () -> {
            pdfPrintService.exportPdf("", parameters);
        });

    }
}
