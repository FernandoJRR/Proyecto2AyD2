package com.ayd.inventory_service.cashRegister.dtos;

import lombok.Value;

@Value
public class SpecificationCashRegisterRequestDTO {
    private String id;
    private String code;
    private Boolean active;
    private String employeeId;
    private String warehouseId;
}
