package com.ayd.employee_service.employees.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePeriod {
    private LocalDate start;
    private LocalDate end;

    public EmployeePeriod(LocalDate start) {
        this.start = start;
    }
}
