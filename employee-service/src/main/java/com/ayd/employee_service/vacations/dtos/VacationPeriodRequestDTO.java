package com.ayd.employee_service.vacations.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VacationPeriodRequestDTO {
    @NotNull(message = "La fecha de inicio de un periodo es obligatoria")
    private LocalDate beginDate;
    @NotNull(message = "La fecha de fin de un periodo es obligatoria")
    private LocalDate endDate;
}
