package com.ayd.inventory_service.supplier.dtos;

import jakarta.validation.constraints.*;
import lombok.Value;
import java.math.BigDecimal;

@Value
public class UpdateSupplierRequestDTO {

    @NotBlank(message = "El NIT es obligatorio")
    @Size(max = 50, message = "El NIT no debe exceder los 50 caracteres")
    String nit;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    String name;

    @NotNull(message = "El régimen tributario es obligatorio")
    @DecimalMin(value = "0.00", message = "El régimen tributario debe ser positivo")
    BigDecimal taxRegime;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no debe exceder los 150 caracteres")
    String address;

    @NotNull(message = "El estado es obligatorio")
    Boolean active;
}
