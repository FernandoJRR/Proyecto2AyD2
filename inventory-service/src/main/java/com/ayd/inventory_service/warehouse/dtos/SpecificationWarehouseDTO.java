package com.ayd.inventory_service.warehouse.dtos;

import lombok.Value;

@Value
public class SpecificationWarehouseDTO {
    private String name;
    private String ubication;
    private Boolean active;
}
