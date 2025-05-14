package com.ayd.sharedInvoiceService.dtos;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.math.BigDecimal;

import com.ayd.sharedInvoiceService.enums.ItemType;

@Value
public class CreateInvoiceDetailRequestDTO {

    @NotBlank(message = "El ID del ítem es obligatorio")
    String itemId;

    @NotBlank(message = "El nombre del ítem es obligatorio")
    String itemName;

    @NotNull(message = "El tipo de ítem es obligatorio")
    ItemType itemType;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio unitario debe ser positivo")
    BigDecimal unitPrice;
}
