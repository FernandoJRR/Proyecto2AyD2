package com.ayd.inventory_service.stock.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ModifyStockRequest {
    @NotBlank(message = "El id del producto no puede estar vacío")
    private String productId;
    @NotNull(message = "La cantidad no puede estar vacía")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer quantity;
    @NotBlank(message = "El id del almacén no puede estar vacío")
    private String warehouseId;
}
