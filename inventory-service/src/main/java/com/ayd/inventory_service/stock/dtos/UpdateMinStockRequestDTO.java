package com.ayd.inventory_service.stock.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UpdateMinStockRequestDTO {
    @NotBlank(message = "El id del producto no puede estar vacío")
    private String productId;
    @NotBlank(message = "El id del almacén no puede estar vacío")
    private String warehouseId;
    @NotNull(message = "El stock mínimo no puede estar vacío")
    @Min(value = 1, message = "El stock mínimo debe ser mayor a 0")
    private Integer minimumStock;
}
