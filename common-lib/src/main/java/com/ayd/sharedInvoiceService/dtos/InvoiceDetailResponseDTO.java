package com.ayd.sharedInvoiceService.dtos;

import lombok.Value;

import java.math.BigDecimal;

import com.ayd.sharedInvoiceService.enums.ItemType;

@Value
public class InvoiceDetailResponseDTO {
    String id;
    String itemId;
    String itemName;
    ItemType itemType;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal total;
}
