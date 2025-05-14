package com.ayd.inventory_service.productEntries.dtos;

import java.time.LocalDate;
import java.util.List;

import com.ayd.inventory_service.supplier.dtos.SupplierResponseDTO;
import com.ayd.sharedInventoryService.warehouse.dto.WarehouseResponseDTO;

import lombok.Value;

@Value
public class ProductEntryResponseDTO {
    String id;
    String invoiceNumber;
    LocalDate date;
    WarehouseResponseDTO warehouse;
    SupplierResponseDTO supplier;
    List<ProductEntryDetailResponseDTO> details;
}
