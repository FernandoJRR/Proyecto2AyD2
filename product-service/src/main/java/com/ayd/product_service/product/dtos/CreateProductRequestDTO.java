package com.ayd.product_service.product.dtos;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre del producto debe tener entre 1 y 100 caracteres")
    private String name;
    @NotBlank(message = "El código del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El código del producto debe tener entre 1 y 100 caracteres")
    private String code;
    @NotBlank(message = "El código de barras del producto es obligatorio")
    @Size(min = 1, max = 100, message = "El código de barras del producto debe tener entre 1 y 100 caracteres")
    private String barCode;
    @NotNull(message = "El tipo de producto es obligatorio")
    private EnumProductType type;
    @NotNull(message = "El estado del producto es obligatorio")
    private EnumProductState state;
}
