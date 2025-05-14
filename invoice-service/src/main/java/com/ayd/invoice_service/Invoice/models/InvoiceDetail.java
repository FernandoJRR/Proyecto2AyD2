package com.ayd.invoice_service.Invoice.models;

import java.math.BigDecimal;

import com.ayd.shared.models.Auditor;
import com.ayd.sharedInvoiceService.enums.ItemType;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvoiceDetail extends Auditor {
    private String itemId; // UUID del producto o paquete

    private String itemName;

    private ItemType itemType; // PRODUCT or PACKAGE

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
