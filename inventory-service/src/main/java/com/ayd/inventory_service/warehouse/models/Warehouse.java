package com.ayd.inventory_service.warehouse.models;

import java.util.List;

import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.warehouse.dtos.CreateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.UpdateWarehouseRequestDTO;
import com.ayd.shared.models.Auditor;

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
public class Warehouse extends Auditor {
    @Column(unique = true, nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String ubication;
    @Column(nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<CashRegister> cashRegisters;
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<Stock> stock;

    public Warehouse(CreateWarehouseRequestDTO createWarehouseRequestDTO) {
        this.name = createWarehouseRequestDTO.getName();
        this.ubication = createWarehouseRequestDTO.getUbication();
        this.active = createWarehouseRequestDTO.isActive();
    }

    public Warehouse updateWarehouse(UpdateWarehouseRequestDTO updateWarehouseRequestDTO) {
        this.name = updateWarehouseRequestDTO.getName();
        this.ubication = updateWarehouseRequestDTO.getUbication();
        this.active = updateWarehouseRequestDTO.isActive();
        return this;
    }

    public boolean toogleActive() {
        this.active = !this.active;
        return this.active;
    }
}
