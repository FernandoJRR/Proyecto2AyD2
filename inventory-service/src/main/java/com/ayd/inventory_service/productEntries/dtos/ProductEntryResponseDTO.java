package com.ayd.inventory_service.productEntries.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Value;

@Value
public class ProductEntryResponseDTO {
    String productEntryId;
    String invoiceNumber;
    LocalDate date;
    String warehouseId;
    String supplierId;
    List<ProductEntryDetailResponseDTO> details;
}
