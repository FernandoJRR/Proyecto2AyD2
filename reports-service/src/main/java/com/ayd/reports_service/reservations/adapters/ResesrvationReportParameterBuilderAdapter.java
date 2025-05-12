package com.ayd.reports_service.reservations.adapters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.reservations.port.ReportParameterBuilderPort;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @param ReportReservationsDTO el dto que contiene la informacion del reporte
 */
@Component
public class ResesrvationReportParameterBuilderAdapter implements ReportParameterBuilderPort<ReportReservationsDTO> {

    private ReportReservationsDTO reportReservationsDTO;

    @Override
    public void init(ReportReservationsDTO reportDTO) {
        this.reportReservationsDTO = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        // convertir la lista de clientes a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource reservations = new JRBeanArrayDataSource(
                reportReservationsDTO.getReservations().toArray());

        // agregar aprams clave al mapa
        params.put("reservations", reservations);
        params.put("totalReservations", reportReservationsDTO.getTotalReservations());

        return params;
    }

}
