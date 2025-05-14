package com.ayd.reports_service.popularhours.adapters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.popularhours.dtos.PopularHoursReportDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@Component
public class PopularHoursReportParameterBuilderAdapter
        implements ReportParameterBuilderPort<PopularHoursReportDTO> {

    private PopularHoursReportDTO report;

    @Override
    public void init(PopularHoursReportDTO reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        // convertir la lista de clientes a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource reservations = new JRBeanArrayDataSource(
                report.getPopularHours().toArray());
        // agregar aprams clave al mapa
        params.put("reservations", reservations);
        params.put("totalReservations", report.getTotalReservations());

        return params;
    }

}
