package com.ayd.reports_service.reservations.port;

import java.util.Map;

public interface ReportParameterBuilderPort<T> {

    public void init(T reportDTO);
    public Map<String, Object> buildParameters();

}
