package com.ayd.inventory_service.supplier.ports;

import java.util.List;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;

public interface ForSupplierPort {
    public Supplier getSupplierById(String id) throws NotFoundException;

    public Supplier getSupplierByName(String name) throws NotFoundException;

    public Supplier saveSupplier(CreateSupplierRequestDTO supplierRequestDTO) throws DuplicatedEntryException;

    public List<Supplier> getAllSuppliers();
}
