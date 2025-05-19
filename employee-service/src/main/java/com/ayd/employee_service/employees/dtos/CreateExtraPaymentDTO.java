package com.ayd.employee_service.employees.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateExtraPaymentDTO {
    @NotBlank(message = "El id del tipo de extra es obligatorio")
    private String paymentTypeId;

    @NotBlank(message = "La razon del extra es obligatorio")
    private String reason;

    @NotNull(message = "La cantidad del extra es obligatoria")
    @Min(1)
    private BigDecimal amount;

    @NotBlank(message = "La descripcion del extra es obligatoria")
    private String description;

    @NotEmpty(message = "Es necesario ingresar a los empleados afectados")
    private List<String> employeesIds;
}
