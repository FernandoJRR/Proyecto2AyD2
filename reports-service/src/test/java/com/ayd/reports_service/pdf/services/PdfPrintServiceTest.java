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

    private Map<String, Object> parameters;

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
