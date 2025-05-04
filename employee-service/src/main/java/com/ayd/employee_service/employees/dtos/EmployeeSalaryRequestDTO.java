package com.ayd.employee_service.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmployeeSalaryRequestDTO {
    @NotNull(message = "Debes ingresar un salario nuevo.")
    @Positive(message = "El salario debe ser un valor positivo.")
    BigDecimal salary;

    @NotNull(message = "Debes ingresar la fecha de cambio de salario.")
    LocalDate salaryDate;
}
