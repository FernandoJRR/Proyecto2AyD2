package com.ayd.product_service.product.dtos;

import lombok.Value;

@Value
public class DeleteProductResponseDTO {
    private String deletedProductId;
    private boolean isDeleted;
    private String message;
}
