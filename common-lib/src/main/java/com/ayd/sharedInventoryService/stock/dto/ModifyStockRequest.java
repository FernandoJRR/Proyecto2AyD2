package com.ayd.sharedInventoryService.stock.dto;

import lombok.Value;

@Value
public class ModifyStockRequest {
    private String productId;
    private Integer quantity;
    private String warehouseId;
}
