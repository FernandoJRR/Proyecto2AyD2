package com.ayd.inventory_service.cashRegister.models;

import com.ayd.inventory_service.cashRegister.dtos.CreateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.UpdateCashRegisterRequestDTO;
import com.ayd.inventory_service.warehouse.models.Warehouse;
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
public class CashRegister extends Auditor {
    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = true, length = 100)
    private String employeeId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public CashRegister(CreateCashRegisterRequestDTO createCashRegisterRequestDTO,Warehouse warehouse) {
        this.code = createCashRegisterRequestDTO.getCode();
        this.active = createCashRegisterRequestDTO.isActive();
        this.employeeId = createCashRegisterRequestDTO.getEmployeeId();
        this.warehouse = warehouse;
    }

    public CashRegister update(UpdateCashRegisterRequestDTO updateCashRegisterRequestDTO,Warehouse warehouse) {
        this.code = updateCashRegisterRequestDTO.getCode();
        this.active = updateCashRegisterRequestDTO.isActive();
        this.employeeId = updateCashRegisterRequestDTO.getEmployeeId();
        this.warehouse = warehouse;
        return this;
    }

    public boolean toogleActive() {
        this.active = !this.active;
        return this.active;
    }
}
