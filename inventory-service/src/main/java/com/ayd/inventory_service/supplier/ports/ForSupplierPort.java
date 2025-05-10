package com.ayd.inventory_service.supplier.ports;

import java.util.List;

import com.ayd.shared.exceptions.*;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.SpecificationSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.UpdateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;

public interface ForSupplierPort {
    public Supplier getSupplierById(String id) throws NotFoundException;

    public Supplier getSupplierByName(String name) throws NotFoundException;

    public Supplier saveSupplier(CreateSupplierRequestDTO supplierRequestDTO) throws DuplicatedEntryException;

    public Supplier updateSupplier(UpdateSupplierRequestDTO supplierRequestDTO,String id) throws NotFoundException,IllegalStateException ;

    public List<Supplier> getAllSuppliers();

    public List<Supplier> getSuppliersBySpecification(SpecificationSupplierRequestDTO specificationSupplierRequestDTO);

    public Supplier toogleSupplierStatus(String id) throws NotFoundException;
}
