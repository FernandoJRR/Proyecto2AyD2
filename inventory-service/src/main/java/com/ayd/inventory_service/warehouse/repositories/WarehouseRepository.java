package com.ayd.inventory_service.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.inventory_service.warehouse.models.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, String>, JpaSpecificationExecutor<Warehouse> {
    public boolean existsByName(String name);
    public boolean existsByNameAndIdNot(String name, String id);
}
