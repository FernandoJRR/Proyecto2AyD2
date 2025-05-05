package com.ayd.inventory_service.supplier.models;

import java.util.List;

import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.shared.models.Auditor;

import jakarta.persistence.CascadeType;
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

     private String nit;

    private String name;

    private String taxRegime; // GEN (12%) or PEQ (5%)

    private String address;

    private boolean active;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<ProductEntry> productEntries;
}
