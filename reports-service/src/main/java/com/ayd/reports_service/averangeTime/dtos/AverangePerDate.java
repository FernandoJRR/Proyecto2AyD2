package com.ayd.reports_service.averangeTime.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;

@Value
public class AverangePerDate {

    LocalDate date;
    BigDecimal averageHours;
}
