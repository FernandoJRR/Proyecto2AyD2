package com.ayd.inventory_service.stock.dtos;

import com.ayd.inventory_service.warehouse.dtos.WarehouseResponseDTO;

import lombok.Value;

@Value
public class StockResponseDTO {
    private String id;
    private Integer quantity;
    private Integer minimumStock;
    private WarehouseResponseDTO warehouse;
}
