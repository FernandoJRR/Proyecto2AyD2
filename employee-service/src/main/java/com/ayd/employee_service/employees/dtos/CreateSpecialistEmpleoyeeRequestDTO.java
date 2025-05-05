package com.ayd.employee_service.employees.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateSpecialistEmpleoyeeRequestDTO {
    @NotBlank(message = "Los nombres del especialista son requeridos")
    @Size(min = 3, max = 100, message = "Los nombres del especialista deben tener entre 3 y 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos del especialista son requeridos")
    @Size(min = 3, max = 100, message = "Los apellidos del especialista deben tener entre 3 y 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El DPI del especialista es requerido")
    @Pattern(regexp = "^[0-9]{13}$", message = "El DPI del especialista debe tener 13 dígitos")
    private String dpi;
}
