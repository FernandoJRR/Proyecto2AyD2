package com.ayd.inventory_service.productEntries.dtos;

import lombok.Value;

@Value
public class ProductEntryDetailResponseDTO {
    private String id;
    private String productId;
    private Integer quantity;
    private Double unitPrice;
}
