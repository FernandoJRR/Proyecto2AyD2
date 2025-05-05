package com.ayd.inventory_service.cashRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.inventory_service.cashRegister.models.CashRegister;

public interface CashRegisterRepository
        extends JpaRepository<CashRegister, String>, JpaSpecificationExecutor<CashRegister> {
    public boolean existsByName(String name);

    public boolean existsByCode(String code);

    public boolean existsByNameAndIdNot(String name, String id);

    public boolean existsByCodeAndIdNot(String code, String id);
}
