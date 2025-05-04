package com.ayd.product_service.product.dtos;

import com.ayd.product_service.product.emuns.EnumProductState;

import lombok.Value;

@Value
public class StateProductResponseDTO {
    private EnumProductState id;
    private String name;
}
