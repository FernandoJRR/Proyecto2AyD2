package com.ayd.inventory_service.productEntries.models;

import java.time.LocalDate;
import java.util.List;

import com.ayd.inventory_service.shared.models.Auditor;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.warehouse.models.Warehouse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductEntry extends Auditor{
    
    private String invoiceNumber;
    
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "productEntry", cascade = CascadeType.ALL)
    private List<ProductEntryDetail> details;
}
