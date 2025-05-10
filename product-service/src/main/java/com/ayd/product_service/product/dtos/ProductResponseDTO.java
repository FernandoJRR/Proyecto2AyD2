package com.ayd.product_service.product.dtos;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class ProductResponseDTO {
    private String id;
    private String name;
    private String code;
    private String barCode;
    private BigDecimal price;
    private String type;
    private String state;
    private String createdAt;
}
