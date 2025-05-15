package com.ayd.reports_service.averangeTime.adapters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.averangeTime.dtos.AverangeTimeReportDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@Component
public class AverageReportParameterAdapter
        implements ReportParameterBuilderPort<AverangeTimeReportDTO> {

    private AverangeTimeReportDTO report;

    @Override
    public void init(AverangeTimeReportDTO reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        // convertir la lista de clientes a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource reservations = new JRBeanArrayDataSource(
                report.getAverangePerDates().toArray());
        // agregar aprams clave al mapa
        params.put("detail", reservations);
        params.put("totalAverage", report.getGlobalHoursAverange());

        return params;
    }

}
