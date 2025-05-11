package com.ayd.reports_service.pdf.services;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ayd.reports_service.pdf.ports.PdfPrinterPort;
import com.ayd.shared.exceptions.ReportGenerationExeption;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class PdfPrintService implements PdfPrinterPort {

    /**
     * Exporta el reporte en formato PDF.
     *
     * @param reportPath Ruta del archivo .jasper
     * @param parameters Parámetros a inyectar en el reporte.
     * @return Un array de bytes que representa el archivo PDF generado.
     * @throws Exception
     */
    @Override
    public byte[] exportPdf(String reportPath, Map<String, Object> parameters) throws ReportGenerationExeption {
        try {
            JasperPrint jasperPrint = this.setParametersToReport(reportPath, parameters);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            return out.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ReportGenerationExeption("Error al generar el reporte PDF: " + e.getMessage());
        }

    }

    /**
     * Método que se encarga de llenar (calcar) el reporte con los datos y
     * parámetros proporcionados.
     *
     * @param reportPath Ruta del archivo .jasper
     * @param parameters Parámetros a inyectar en el reporte.
     * @return El objeto JasperPrint resultante.
     * @throws Exception
     */
    private JasperPrint setParametersToReport(String reportPath, Map<String, Object> parameters) throws Exception {
        URL resourceUrl = getClass().getResource("/static/reports" + reportPath + ".jasper");

        if (resourceUrl == null) {
            throw new Exception("El reporte no existe en la ruta especificada");
        }

        // Cargar el reporte .jasper desde la ruta especificada
        JasperReport reporte = (JasperReport) JRLoader
                .loadObject(resourceUrl);

        // Llenar el reporte con los datos
        return JasperFillManager.fillReport(reporte, parameters, new JREmptyDataSource());
    }

}
