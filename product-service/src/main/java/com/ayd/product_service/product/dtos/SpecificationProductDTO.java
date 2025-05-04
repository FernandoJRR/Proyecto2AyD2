package com.ayd.product_service.product.dtos;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationProductDTO {
    private String id;
    private String name;
    private String code;
    private String barCode;
    private EnumProductType type;
    private EnumProductState state;
}
