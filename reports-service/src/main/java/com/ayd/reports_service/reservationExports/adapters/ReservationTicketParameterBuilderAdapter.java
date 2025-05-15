package com.ayd.reports_service.reservationExports.adapters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ayd.reports_service.shared.ports.ReportParameterBuilderPort;
import com.ayd.sharedReservationService.dto.ReservationResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationTicketParameterBuilderAdapter implements ReportParameterBuilderPort<ReservationResponseDTO> {

    private ReservationResponseDTO report;

    @Override
    public void init(ReservationResponseDTO reportDTO) {
        this.report = reportDTO;
    }

    @Override
    public Map<String, Object> buildParameters() {
        // crear el mapa de params para el reporte
        Map<String, Object> params = new HashMap<>();

        params.put("reservationId", report.getId());
        params.put("clientNit", report.getCustomerNIT());
        params.put("clientFullname", report.getCustomerFullName());
        params.put("date", report.getDate());
        params.put("startTime", report.getStartTime());
        params.put("endTime", report.getEndTime());

        return params;
    }

}
