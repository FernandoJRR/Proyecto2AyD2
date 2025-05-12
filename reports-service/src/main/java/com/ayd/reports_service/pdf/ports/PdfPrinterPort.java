package com.ayd.reports_service.pdf.ports;

import java.util.Map;

import com.ayd.shared.exceptions.ReportGenerationExeption;

public interface PdfPrinterPort {

    public byte[] exportPdf(String reportPath, Map<String, Object> parameters) throws ReportGenerationExeption;

}
