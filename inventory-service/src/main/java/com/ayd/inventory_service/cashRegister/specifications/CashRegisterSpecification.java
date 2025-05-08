package com.ayd.inventory_service.cashRegister.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ayd.inventory_service.cashRegister.models.CashRegister;

public class CashRegisterSpecification {
    public static Specification<CashRegister> hasCode(String code) {
        return (root, query, cb) -> code == null ? null
                : cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    public static Specification<CashRegister> hasActive(Boolean active) {
        return (root, query, cb) -> active == null ? null : cb.equal(root.get("active"), active);
    }

    public static Specification<CashRegister> hasWarehouseId(String warehouseId) {
        return (root, query, cb) -> warehouseId == null ? null
                : cb.equal(root.join("warehouse").get("id"), warehouseId);
    }

    public static Specification<CashRegister> hasEmployeeId(String employeeId) {
        return (root, query, cb) -> employeeId == null ? null
                : cb.equal(root.get("employeeId"), employeeId);
    }
}
