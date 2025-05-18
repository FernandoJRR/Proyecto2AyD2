package com.ayd.config_service.parameters.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class ParameterDiasRequestDTO {
    @NotBlank(message = "Los nuevos dias de vacaciones son obligatorios")
    @Pattern(regexp = "\\d+", message = "Los dias de vacaciones debe ser un valor numerico")
    private String newDays;
}
