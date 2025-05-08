package com.ayd.inventory_service.productEntries.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class ProductEntryDetailRequestDTO {
    @NotBlank(message = "El id del producto es obligatorio")
    private String productId;
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    private Integer quantity;
    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor que cero")
    private BigDecimal unitPrice;
}
