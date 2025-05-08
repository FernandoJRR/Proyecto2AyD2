package com.ayd.inventory_service.supplier.services;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.SpecificationSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.UpdateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.ports.ForSupplierPort;
import com.ayd.inventory_service.supplier.repositories.SupplierRepository;
import com.ayd.inventory_service.supplier.specifications.SupplierSpecification;

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

    @Override
    public Supplier updateSupplier(UpdateSupplierRequestDTO supplierRequestDTO, String id)
            throws NotFoundException, IllegalStateException {
        Supplier supplier = getSupplierById(id);
        supplier = supplier.update(supplierRequestDTO);
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

    @Override
    public List<Supplier> getSuppliersBySpecification(SpecificationSupplierRequestDTO specificationSupplierRequestDTO) {
        if (specificationSupplierRequestDTO == null) {
            return getAllSuppliers();
        }
        Specification<Supplier> specification = Specification
                .where(SupplierSpecification.hasName(specificationSupplierRequestDTO.getName()))
                .and(SupplierSpecification.hasNit(specificationSupplierRequestDTO.getNit()))
                .and(SupplierSpecification.hasTaxRegime(specificationSupplierRequestDTO.getTaxRegime()))
                .and(SupplierSpecification.hasAddress(specificationSupplierRequestDTO.getAddress()))
                .and(SupplierSpecification.isActive(specificationSupplierRequestDTO.getActive()));
        List<Supplier> suppliers = supplierRepository.findAll(specification);
        return suppliers;
    }

    @Override
    public Supplier toogleSupplierStatus(String id) throws NotFoundException {
        Supplier supplier = getSupplierById(id);
        supplier.toogleActive();
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

}
