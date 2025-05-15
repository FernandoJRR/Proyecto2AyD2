package com.ayd.reports_service.averangeTime.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

@Value
public class AverangeTimeReportDTO {

    BigDecimal globalHoursAverange;
    List<AverangePerDate> averangePerDates;
}
