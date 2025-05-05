package com.ayd.employee_service.vacations.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeVacationDaysRequestDTO {
    @NotNull(message = "La nueva cantidad de dias es obligatoria")
    @Min(value = 1)
    @Max(value = 60)
    private Integer newVacationDays;
}
