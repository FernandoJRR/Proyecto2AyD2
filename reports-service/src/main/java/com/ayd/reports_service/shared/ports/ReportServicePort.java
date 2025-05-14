package com.ayd.reports_service.shared.ports;

import com.ayd.shared.exceptions.ReportGenerationExeption;

public interface ReportServicePort<R,F> {

    
    public R generateReport(F filters);
    public byte[] generateReportAsPdf(F filters) throws ReportGenerationExeption;

}
