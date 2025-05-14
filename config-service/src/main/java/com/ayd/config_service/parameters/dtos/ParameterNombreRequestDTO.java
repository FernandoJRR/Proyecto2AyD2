package com.ayd.config_service.parameters.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ParameterNombreRequestDTO {
    @NotBlank(message = "El nuevo nombre de la empresa es obligatorio.")
    private String newName;
}
