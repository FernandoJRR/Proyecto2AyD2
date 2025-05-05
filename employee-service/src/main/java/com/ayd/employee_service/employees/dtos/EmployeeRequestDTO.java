package com.ayd.employee_service.employees.dtos;

import java.math.BigDecimal;

import com.ayd.employee_service.shared.dtos.IdRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmployeeRequestDTO {
    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 100, message = "El primer nombre no puede tener más de 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String lastName;

    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El salario debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El salario debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal salary;

    @NotBlank(message = "El CUI es obligatorio")
    @Pattern(regexp = "^[0-9]{13}$", message = "El CUI debe ser un numero entero de 13 digitos")
    private String cui;

    @DecimalMin(value = "0.00", inclusive = true, message = "El porcentaje de IGSS no puede ser negativo")
    @DecimalMax(value = "100.00", inclusive = true, message = "El porcentaje de IGSS no puede ser mayor a 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje de IGSS debe tener hasta 3 dígitos enteros y 2 decimales")
    private BigDecimal igssPercentage;

    @DecimalMin(value = "0.00", inclusive = true, message = "El porcentaje de IRTRA no puede ser negativo")
    @DecimalMax(value = "100.00", inclusive = true, message = "El porcentaje de IRTRA no puede ser mayor a 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje de IRTRA debe tener hasta 3 dígitos enteros y 2 decimales")
    private BigDecimal irtraPercentage;

    @Valid
    @NotNull(message = "El employeeTypeId no puede ser nulo")
    private IdRequestDTO employeeTypeId;
}
