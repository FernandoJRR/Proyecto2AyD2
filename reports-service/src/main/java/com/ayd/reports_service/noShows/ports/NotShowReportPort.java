package com.ayd.reports_service.noShows.ports;

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.reports_service.shared.ports.ReportServicePort;
import com.ayd.shared.dtos.PeriodRequestDTO;

public interface NotShowReportPort extends ReportServicePort<ReportReservationsDTO, PeriodRequestDTO> {

}
