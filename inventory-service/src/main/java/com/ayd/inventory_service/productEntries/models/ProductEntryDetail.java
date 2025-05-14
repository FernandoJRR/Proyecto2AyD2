package com.ayd.inventory_service.productEntries.models;

import java.math.BigDecimal;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
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
public class ProductEntryDetail extends Auditor {
    @Column(nullable = false, length = 100)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "product_entry_id")
    private ProductEntry productEntry;

    public ProductEntryDetail(ProductEntryDetailRequestDTO productEntryDetailRequestDTO, ProductEntry productEntry) {
        this.productId = productEntryDetailRequestDTO.getProductId();
        this.quantity = productEntryDetailRequestDTO.getQuantity();
        this.unitPrice = productEntryDetailRequestDTO.getUnitPrice();
        this.productEntry = productEntry;
    }
}
