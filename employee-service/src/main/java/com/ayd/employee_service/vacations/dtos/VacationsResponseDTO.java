package com.ayd.employee_service.vacations.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacationsResponseDTO {
    private String id;
    private Integer periodYear;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Boolean wasUsed;
}
