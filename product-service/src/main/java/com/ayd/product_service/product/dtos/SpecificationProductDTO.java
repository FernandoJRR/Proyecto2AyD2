package com.ayd.product_service.product.dtos;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;

import lombok.Value;

@Value
public class SpecificationProductDTO {
    private String id;
    private String name;
    private String code;
    private String barCode;
    private EnumProductType type;
    private EnumProductState state;
}
