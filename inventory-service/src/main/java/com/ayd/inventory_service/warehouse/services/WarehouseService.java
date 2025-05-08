package com.ayd.inventory_service.warehouse.services;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.warehouse.dtos.CreateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.SpecificationWarehouseDTO;
import com.ayd.inventory_service.warehouse.dtos.UpdateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;
import com.ayd.inventory_service.warehouse.repositories.WarehouseRepository;
import com.ayd.inventory_service.warehouse.specifications.WarehouseSpecification;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class WarehouseService implements ForWarehousePort {

    private final WarehouseRepository warehouseRepository;

    @Override
    public Warehouse createWarehouse(CreateWarehouseRequestDTO createWarehouseRequestDTO)
            throws DuplicatedEntryException {
        if (warehouseRepository.existsByName(createWarehouseRequestDTO.getName())) {
            throw new DuplicatedEntryException("Ya existe un bodega con el nombre: "
                    + createWarehouseRequestDTO.getName());
        }
        Warehouse warehouse = new Warehouse(createWarehouseRequestDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouse;
    }

    @Override
    public Warehouse updateWarehouse(String id, UpdateWarehouseRequestDTO updateWarehouseRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro la bodega con id: " + id));
        if (warehouseRepository.existsByNameAndIdNot(id, updateWarehouseRequestDTO.getName())) {
            throw new DuplicatedEntryException("Ya existe un bodega con el nombre: "
                    + updateWarehouseRequestDTO.getName());
        }
        warehouse = warehouse.updateWarehouse(updateWarehouseRequestDTO);
        warehouse = warehouseRepository.save(warehouse);
        return warehouse;
    }

    @Override
    public boolean deleteWarehouse(String id) throws NotFoundException {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro la bodega con id: " + id));
        warehouseRepository.delete(warehouse);
        return true;
    }

    @Override
    public Warehouse getWarehouse(String id) throws NotFoundException {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro la bodega con id: " + id));
        return warehouse;
    }

    @Override
    public List<Warehouse> getWarehouses(SpecificationWarehouseDTO specificationWarehouseDTO) {
        if (specificationWarehouseDTO == null) {
            return warehouseRepository.findAll();
        }
        Specification<Warehouse> spec = Specification
                .where(WarehouseSpecification.hasName(specificationWarehouseDTO.getName()))
                .and(WarehouseSpecification.hasUbication(specificationWarehouseDTO.getUbication()))
                .and(WarehouseSpecification.isActive(specificationWarehouseDTO.getActive()));
        return warehouseRepository.findAll(spec);
    }

    @Override
    public Warehouse tootgleActive(String id) throws NotFoundException {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontro la bodega con id: " + id));
        warehouse.toogleActive();
        warehouseRepository.save(warehouse);
        return warehouse;
    }

}
