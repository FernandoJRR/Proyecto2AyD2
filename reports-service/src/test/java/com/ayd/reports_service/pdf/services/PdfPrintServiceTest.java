package com.ayd.reports_service.pdf.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.shared.exceptions.ReportGenerationExeption;

@ExtendWith(MockitoExtension.class)
public class PdfPrintServiceTest {

    private static final String TEST_REPORT_PATH = "/ReservationsReport";
    private Map<String, Object> parameters;
    private static final byte[] PDF_BYTES = new byte[] { 1, 2, 3 };

    @InjectMocks
    private PdfPrintService pdfPrintService;

    @BeforeEach
    void setUp() {
        parameters = Map.of();
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
