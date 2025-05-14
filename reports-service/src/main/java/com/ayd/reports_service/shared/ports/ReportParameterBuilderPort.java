package com.ayd.reports_service.shared.ports;

import java.util.Map;

public interface ReportParameterBuilderPort<T> {

    public void init(T reportDTO);
    public Map<String, Object> buildParameters();

}
