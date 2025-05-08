package com.ayd.inventory_service.productEntries.dtos;

import java.time.LocalDate;

import lombok.Value;

@Value
public class ProductEntrySpecificationDTO {
    private String invoiceNumber;
    private String id;
    private String warehouseId;
    private String supplierId;
    private LocalDate date;
}
