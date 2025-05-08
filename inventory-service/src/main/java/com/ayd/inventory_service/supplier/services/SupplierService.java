package com.ayd.inventory_service.supplier.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.ports.ForSupplierPort;
import com.ayd.inventory_service.supplier.repositories.SupplierRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class SupplierService implements ForSupplierPort {

    private final SupplierRepository supplierRepository;

    @Override
    public Supplier getSupplierById(String id) throws NotFoundException {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El proveedor no existe"));
    }

    @Override
    public Supplier getSupplierByName(String name) throws NotFoundException {
        return supplierRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("El proveedor no existe"));
    }

    @Override
    public Supplier saveSupplier(CreateSupplierRequestDTO supplierRequestDTO) throws DuplicatedEntryException {
        if (supplierRepository.existsByName(supplierRequestDTO.getName())) {
            throw new DuplicatedEntryException("El proveedor ya existe");
        }
        if (supplierRepository.existsByNit(supplierRequestDTO.getNit())) {
            throw new DuplicatedEntryException("El NIT ya existe");
        }
        Supplier supplier = new Supplier(supplierRequestDTO);
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers;
    }

}
