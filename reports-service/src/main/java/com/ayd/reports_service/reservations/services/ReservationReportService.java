package com.ayd.reports_service.reservations.services;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.reports_service.shared.services.ReportServicePort;
import com.ayd.shared.exceptions.ReportGenerationExeption;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationReportService implements ReportServicePort<Object> {

    private final PdfPrinterPort pdfPrinterPort;

    public Object generateReport(Object filters) {
        return null;
    }

    public byte[] generateReportAsPdf() throws ReportGenerationExeption {
        return pdfPrinterPort.exportPdf(null, null);
    }

}
