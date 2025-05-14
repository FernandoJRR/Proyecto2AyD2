package com.ayd.sharedProductService.packages.dtos;

import com.ayd.sharedProductService.product.dtos.ProductResponseDTO;

import lombok.Value;

@Value
public class GolfPackageDetailResponseDTO {
    String id;
    ProductResponseDTO product;
    Integer quantity;
}
