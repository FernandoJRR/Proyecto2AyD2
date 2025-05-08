package com.ayd.inventory_service.warehouse.dtos;

import lombok.Value;

@Value
public class WarehouseResponseDTO {
    private String id;
    private String name;
    private String ubication;
    private boolean active;
}
