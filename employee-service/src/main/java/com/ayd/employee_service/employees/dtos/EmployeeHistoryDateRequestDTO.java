package com.ayd.employee_service.employees.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeHistoryDateRequestDTO {
    @NotNull(message = "La fecha del historial es obligatoria")
    private LocalDate historyDate;
}
