package com.ayd.product_service.product.dtos;

import java.math.BigDecimal;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateProductRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre del producto debe tener entre 1 y 100 caracteres")
    private String name;
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El código del producto debe tener entre 1 y 100 caracteres")
    private String code;
    @NotBlank(message = "El código de barras del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El código de barras del producto debe tener entre 1 y 100 caracteres")
    private String barCode;
    @NotNull(message = "El precio del producto es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor que cero")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal price;
    @NotNull(message = "El tipo de producto es obligatorio")
    private EnumProductType type;
    @NotNull(message = "El estado del producto es obligatorio")
    private EnumProductState state;
}
