package com.ayd.inventory_service.productEntries.models;

import java.math.BigDecimal;

import com.ayd.inventory_service.shared.models.Auditor;

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
    private String productId;

    private Integer quantity;

    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "product_entry_id")
    private ProductEntry productEntry;
}
