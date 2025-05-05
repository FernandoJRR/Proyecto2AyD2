package com.ayd.inventory_service.cashRegister.dtos;

import lombok.Value;

@Value
public class SpecificationCashRegisterRequestDTO {
    private String name;
    private String code;
    private Boolean active;
    private String warehouseId;
}
