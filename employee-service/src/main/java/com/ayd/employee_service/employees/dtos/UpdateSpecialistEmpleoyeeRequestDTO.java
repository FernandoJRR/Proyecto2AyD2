package com.ayd.employee_service.employees.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateSpecialistEmpleoyeeRequestDTO {
    @Size(min = 3, max = 100, message = "Los nombres del especialista deben tener entre 3 y 100 caracteres")
    private String nombres;

    @Size(min = 3, max = 100, message = "Los apellidos del especialista deben tener entre 3 y 100 caracteres")
    private String apellidos;

    @Pattern(regexp = "^[0-9]{13}$", message = "El DPI del especialista debe tener 13 dígitos")
    private String dpi;
}
