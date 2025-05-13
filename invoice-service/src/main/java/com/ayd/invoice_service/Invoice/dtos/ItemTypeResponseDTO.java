package com.ayd.invoice_service.Invoice.dtos;

import com.ayd.invoice_service.Invoice.enums.ItemType;

import lombok.Value;

@Value
public class ItemTypeResponseDTO {
    private ItemType itemType;
    private String name;
}
