package com.ayd.invoice_service.Invoice.dtos;

import com.ayd.invoice_service.Invoice.enums.ItemType;
import lombok.Value;

import java.math.BigDecimal;

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
