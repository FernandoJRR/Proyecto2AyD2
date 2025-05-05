package com.ayd.employee_service.employees.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeTypeDTO {
    @NotBlank(message = "El nombre del tipo de empleado no puede estar vacío o nulo.")
    @Size(max = 100, message = "El nombre del tipo de empleado no puede tener más de 100 caracteres.")
    private String name;

}
