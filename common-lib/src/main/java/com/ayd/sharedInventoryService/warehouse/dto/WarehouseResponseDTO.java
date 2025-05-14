package com.ayd.sharedInventoryService.warehouse.dto;

import lombok.Value;

@Value
public class WarehouseResponseDTO {
    private String id;
    private String name;
    private String ubication;
    private boolean active;
}
