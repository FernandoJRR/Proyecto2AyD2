package com.ayd.shared.dtos;

import java.time.LocalDate;

import lombok.Value;

@Value
public class PeriodRequestDTO {

    LocalDate startDate;
    LocalDate endDate;
}
