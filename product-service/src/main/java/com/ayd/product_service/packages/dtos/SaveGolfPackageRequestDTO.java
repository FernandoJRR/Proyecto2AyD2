package com.ayd.product_service.packages.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class SaveGolfPackageRequestDTO {

    @NotBlank(message = "El nombre del paquete es obligatorio")
    @Size(max = 250, message = "El nombre no puede superar los 250 caracteres")
    String name;

    @NotBlank(message = "La descripción del paquete es obligatoria")
    String description;

    @NotNull(message = "El precio del paquete es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor que cero")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    BigDecimal price;

    @NotEmpty(message = "Debe proporcionar al menos un producto en el paquete")
    @Valid
    List<GolfPackageDetailRequestDTO> packageDetail;

    @NotNull(message = "El estado del paquete es obligatorio")
    Boolean active;

}
