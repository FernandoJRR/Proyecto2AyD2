package com.ayd.sharedInventoryService.cashRegister.dto;

import com.ayd.sharedInventoryService.warehouse.dto.WarehouseResponseDTO;

import lombok.Value;

@Value
public class CashRegisterResponseDTO {
    private String id;
    private String code;
    private String employeeId;
    private boolean active;
    private WarehouseResponseDTO warehouse;
}
