package com.ayd.employee_service.employees.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeReactivateRequestDTO {
    private LocalDate reactivationDate;
}
