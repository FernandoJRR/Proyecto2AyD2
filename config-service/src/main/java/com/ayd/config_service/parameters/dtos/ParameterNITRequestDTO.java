package com.ayd.config_service.parameters.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class ParameterNITRequestDTO {
    @NotBlank(message = "El nuevo tipo de regimen es obligatorio")
    @Pattern(regexp = "\\d+", message = "El NIT debe ser un valor numerico")
    private String nit;

}
