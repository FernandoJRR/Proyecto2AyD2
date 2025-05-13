package com.ayd.config_service.parameters.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ParameterRegimenRequestDTO {
    @NotBlank(message = "El nuevo tipo de regimen es obligatorio")
    @Pattern(regexp = "gen|peq", message = "El valor del nuevo regimen debe ser 'gen' o 'peq'")
    private String newRegime;
}
