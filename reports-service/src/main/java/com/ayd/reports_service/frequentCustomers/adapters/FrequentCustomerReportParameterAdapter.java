package com.ayd.reports_service.frequentCustomers.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.frequentCustomers.dtos.FrequentCustomerDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @param ReportReservationsDTO el dto que contiene la informacion del reporte
 */
@Component
public class FrequentCustomerReportParameterAdapter
        implements ReportParameterBuilderPort<List<FrequentCustomerDTO>> {

    private List<FrequentCustomerDTO> report;

    @Override
    public void init(List<FrequentCustomerDTO> reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        // convertir la lista de clientes a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource reservations = new JRBeanArrayDataSource(
                report.toArray());

        // agregar aprams clave al mapa
        params.put("detail", reservations);

        return params;
    }

}
