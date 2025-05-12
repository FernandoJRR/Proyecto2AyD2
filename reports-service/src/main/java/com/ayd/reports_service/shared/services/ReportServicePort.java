package com.ayd.reports_service.shared.services;

import com.ayd.reports_service.reservations.dto.ReportReservationsDTO;
import com.ayd.shared.exceptions.ReportGenerationExeption;

public interface ReportServicePort<F> {

    
    public ReportReservationsDTO generateReport(F filters);
    public byte[] generateReportAsPdf(F filters) throws ReportGenerationExeption;

}
