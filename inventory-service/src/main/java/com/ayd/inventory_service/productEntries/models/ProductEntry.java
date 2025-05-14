package com.ayd.inventory_service.productEntries.models;

import java.time.LocalDate;
import java.util.List;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class ProductEntry extends Auditor {

    @Column(unique = true, nullable = false, length = 100)
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

    public ProductEntry(ProductEntryRequestDTO productEntryRequestDTO, Warehouse warehouse, Supplier supplier) {
        this.invoiceNumber = productEntryRequestDTO.getInvoiceNumber();
        this.date = productEntryRequestDTO.getDate();
        this.warehouse = warehouse;
        this.supplier = supplier;
    }

}
