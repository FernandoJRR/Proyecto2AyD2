package com.ayd.inventory_service.cashRegister.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCashRegisterRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre del producto debe tener entre 1 y 100 caracteres")
    private String name;
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, max = 50, message = "El nombre del producto debe tener entre 1 y 50 caracteres")
    private String code;
    @NotNull(message = "El estado del producto es obligatorio")
    private boolean active;
    @NotBlank(message = "El id del almacén es obligatorio")
    private String warehouseId;
}
