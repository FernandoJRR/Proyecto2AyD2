package com.ayd.inventory_service.supplier.models;

import java.math.BigDecimal;
import java.util.List;

import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.shared.models.Auditor;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Supplier extends Auditor {

    @Column(unique = true, nullable = false, length = 50)
    private String nit;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal taxRegime; // GEN (12%) or PEQ (5%)

    @Column(nullable = false, length = 150)
    private String address;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<ProductEntry> productEntries;

    public Supplier(CreateSupplierRequestDTO supplierRequestDTO) {
        this.nit = supplierRequestDTO.getNit();
        this.name = supplierRequestDTO.getName();
        this.taxRegime = supplierRequestDTO.getTaxRegime();
        this.address = supplierRequestDTO.getAddress();
        this.active = supplierRequestDTO.getActive();
    }
}
