package com.ayd.product_service.product.dtos;

import com.ayd.product_service.product.emuns.EnumProductType;

import lombok.Value;

@Value
public class TypeProductResponseDTO {
    private EnumProductType id;
    private String name;
}
