package com.ayd.product_service.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class DeleteProductRequestDTO {
    private String id;
}
