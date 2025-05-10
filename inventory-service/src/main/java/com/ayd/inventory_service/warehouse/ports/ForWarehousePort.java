package com.ayd.inventory_service.warehouse.ports;

import java.util.List;

import com.ayd.shared.exceptions.*;
import com.ayd.inventory_service.warehouse.dtos.CreateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.SpecificationWarehouseDTO;
import com.ayd.inventory_service.warehouse.dtos.UpdateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.models.Warehouse;

public interface ForWarehousePort {
    public Warehouse createWarehouse(CreateWarehouseRequestDTO createWarehouseRequestDTO)
            throws DuplicatedEntryException;

    public Warehouse updateWarehouse(String id, UpdateWarehouseRequestDTO updateWarehouseRequestDTO)
            throws DuplicatedEntryException, NotFoundException;

    public boolean deleteWarehouse(String id) throws NotFoundException;

    public Warehouse getWarehouse(String id) throws NotFoundException;

    public List<Warehouse> getWarehouses(SpecificationWarehouseDTO specificationWarehouseDTO);

    public Warehouse tootgleActive(String id) throws NotFoundException;
}
