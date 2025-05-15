package com.ayd.reports_service.reservationProfit.adapters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.reservationProfit.dtos.ReservationProfitDTO;
import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@Component
@RequiredArgsConstructor
public class ReservationProfitParametersAdapter implements ReportParameterBuilderPort<ReservationProfitDTO> {

    private ReservationProfitDTO report;

    @Override
    public void init(ReservationProfitDTO reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        // convertir la lista de la factura a un JRBeanArrayDataSource para
        // JasperReports
        JRBeanArrayDataSource detail = new JRBeanArrayDataSource(
                report.getReservations().toArray());

        params.put("detail", detail);
        params.put("total", report.getTotalProfit());
        return params;
    }

}
