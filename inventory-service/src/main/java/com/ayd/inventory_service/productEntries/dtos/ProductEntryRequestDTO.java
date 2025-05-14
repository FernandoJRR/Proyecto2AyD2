package com.ayd.inventory_service.productEntries.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class ProductEntryRequestDTO {
    @NotBlank(message = "El numero de factura es obligatorio")
    @Size(max = 100, message = "El numero de factura no puede exceder los 100 caracteres")
    private String invoiceNumber;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;
    @NotBlank(message = "El id del almacen es obligatorio")
    private String warehouseId;
    @NotBlank(message = "El id del proveedor es obligatorio")
    private String supplierId;
    @NotNull(message = "Los detalles son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle")
    private List<ProductEntryDetailRequestDTO> details;
}
