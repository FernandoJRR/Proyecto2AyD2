package com.ayd.inventory_service.supplier.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.inventory_service.supplier.models.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, String> , JpaSpecificationExecutor<Supplier> {
    public boolean existsByName(String name);
    public boolean existsByNit(String nit);
    public boolean existsByNameAndIdNot(String name, String id);
    public boolean existsByNitAndIdNot(String nit, String id);
}
