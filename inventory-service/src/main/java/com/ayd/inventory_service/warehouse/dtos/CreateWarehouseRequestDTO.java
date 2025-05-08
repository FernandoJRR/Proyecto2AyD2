package com.ayd.inventory_service.warehouse.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateWarehouseRequestDTO {
    @NotBlank(message = "El nombre de la bodega no puede estar vacío")
    @Size(min = 1, max = 100, message = "El nombre de la bodega debe tener entre 1 y 100 caracteres")
    private String name;
    @NotBlank(message = "La ubicación de la bodega no puede estar vacía")
    @Size(min = 1, max = 100, message = "La ubicación de la bodega debe tener entre 1 y 100 caracteres")
    private String ubication;
    @NotNull(message = "El estado de la bodega no puede estar vacío")
    private boolean active;
}
