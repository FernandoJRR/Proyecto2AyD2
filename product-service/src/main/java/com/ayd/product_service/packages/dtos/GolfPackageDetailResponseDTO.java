package com.ayd.product_service.packages.dtos;

import com.ayd.product_service.product.dtos.ProductResponseDTO;

import lombok.Value;

@Value
public class GolfPackageDetailResponseDTO {
    String id;
    ProductResponseDTO product;
    Integer quantity;
}
