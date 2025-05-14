package com.ayd.inventory_service.cashRegister.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateCashRegisterRequestDTO {
    @NotBlank(message = "Se debe de agregar un codigo a la caja")
    @Size(min = 1, max = 50, message = "El nombre del producto debe tener entre 1 y 50 caracteres")
    private String code;
    @NotNull(message = "El estado de la caja es obligatorio")
    private boolean active;
    @NotBlank(message = "El id del almacén es obligatorio")
    private String warehouseId;
    private String employeeId; //El empleado es opcional, ya que puede no estar asignado a la caja al momento de crearla
}
