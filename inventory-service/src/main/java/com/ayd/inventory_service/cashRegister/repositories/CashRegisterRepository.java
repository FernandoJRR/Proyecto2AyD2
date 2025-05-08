package com.ayd.inventory_service.cashRegister.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.inventory_service.cashRegister.models.CashRegister;

public interface CashRegisterRepository
        extends JpaRepository<CashRegister, String>, JpaSpecificationExecutor<CashRegister> {
    public boolean existsByCode(String code);

    public boolean existsByCodeAndIdNot(String code, String id);

    public boolean existsByEmployeeId(String employeeId);

    public boolean existsByEmployeeIdAndIdNot(String employeeId, String id);

    public Optional<CashRegister> findByCode(String code);
}
