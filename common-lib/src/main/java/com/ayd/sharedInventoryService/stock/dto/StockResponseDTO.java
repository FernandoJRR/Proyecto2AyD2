package com.ayd.sharedInventoryService.stock.dto;

import com.ayd.sharedInventoryService.warehouse.dto.WarehouseResponseDTO;

import lombok.Value;

@Value
public class StockResponseDTO {
    private String id;
    private Integer quantity;
    private Integer minimumStock;
    private WarehouseResponseDTO warehouse;
}
