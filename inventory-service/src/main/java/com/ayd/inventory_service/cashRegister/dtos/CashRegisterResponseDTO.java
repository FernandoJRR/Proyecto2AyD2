package com.ayd.inventory_service.cashRegister.dtos;

import com.ayd.inventory_service.warehouse.dtos.WarehouseResponseDTO;

import lombok.Value;

@Value
public class CashRegisterResponseDTO {
    private Long id;
    private String name;
    private String active;
    private WarehouseResponseDTO warehouse;
}
