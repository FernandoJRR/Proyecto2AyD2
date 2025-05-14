package com.ayd.inventory_service.cashRegister.services;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.inventory_service.cashRegister.dtos.CreateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.SpecificationCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.UpdateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.inventory_service.cashRegister.ports.ForCashRegisterPort;
import com.ayd.inventory_service.cashRegister.repositories.CashRegisterRepository;
import com.ayd.inventory_service.cashRegister.specifications.CashRegisterSpecification;
import com.ayd.shared.exceptions.*;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class CashRegisterService implements ForCashRegisterPort {

    private final CashRegisterRepository cashRegisterRepository;
    private final ForWarehousePort forWarehousePort;

    @Override
    public List<CashRegister> findAll() {
        return cashRegisterRepository.findAll();
    }

    @Override
    public List<CashRegister> findAllBySpecification(
            SpecificationCashRegisterRequestDTO specificationCashRegisterRequestDTO) {
        if (specificationCashRegisterRequestDTO == null) {
            return findAll();
        }
        Specification<CashRegister> spec = Specification
                .where(CashRegisterSpecification.hasCode(specificationCashRegisterRequestDTO.getCode()))
                .and(CashRegisterSpecification.hasActive(specificationCashRegisterRequestDTO.getActive()))
                .and(CashRegisterSpecification.hasWarehouseId(specificationCashRegisterRequestDTO.getWarehouseId()))
                .and(CashRegisterSpecification.hasEmployeeId(specificationCashRegisterRequestDTO.getEmployeeId()));
        return cashRegisterRepository.findAll(spec);
    }

    @Override
    public CashRegister findById(String id) throws NotFoundException {
        return cashRegisterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el registro de caja con id: " + id));
    }

    @Override
    public CashRegister findByCode(String code) throws NotFoundException {
        return cashRegisterRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("No se encontró el registro de caja con código: " + code));
    }

    @Override
    public CashRegister save(CreateCashRegisterRequestDTO createCashRegisterRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(createCashRegisterRequestDTO.getWarehouseId());
        if (cashRegisterRepository.existsByCode(createCashRegisterRequestDTO.getCode())) {
            throw new DuplicatedEntryException("Ya existe un registro de caja con el código: "
                    + createCashRegisterRequestDTO.getCode());
        }
        if (createCashRegisterRequestDTO.getEmployeeId() != null) {
            if (cashRegisterRepository.existsByEmployeeId(createCashRegisterRequestDTO.getEmployeeId())) {
                throw new DuplicatedEntryException("Ya existe un registro de caja con el empleado: "
                        + createCashRegisterRequestDTO.getEmployeeId());
            }
        }
        CashRegister cashRegister = new CashRegister(createCashRegisterRequestDTO, warehouse);
        cashRegister = cashRegisterRepository.save(cashRegister);
        return cashRegister;
    }

    @Override
    public CashRegister update(String id, UpdateCashRegisterRequestDTO updateCashRegisterRequestDTO)
            throws NotFoundException, DuplicatedEntryException {
        CashRegister cashRegister = findById(id);
        if (cashRegisterRepository.existsByCodeAndIdNot(updateCashRegisterRequestDTO.getCode(), id)) {
            throw new DuplicatedEntryException("Ya existe un registro de caja con el código: "
                    + updateCashRegisterRequestDTO.getCode());
        }
        if (updateCashRegisterRequestDTO.getEmployeeId() != null) {
            if (cashRegisterRepository.existsByEmployeeIdAndIdNot(updateCashRegisterRequestDTO.getEmployeeId(), id)) {
                throw new DuplicatedEntryException("Ya existe un registro de caja con el empleado: "
                        + updateCashRegisterRequestDTO.getEmployeeId());
            }
        }
        Warehouse warehouse = forWarehousePort.getWarehouse(updateCashRegisterRequestDTO.getWarehouseId());
        cashRegister = cashRegister.update(updateCashRegisterRequestDTO, warehouse);
        return cashRegister;
    }

    @Override
    public CashRegister toggleActive(String id) throws NotFoundException {
        CashRegister cashRegister = findById(id);
        cashRegister.toogleActive();
        cashRegister = cashRegisterRepository.save(cashRegister);
        return cashRegister;
    }

    @Override
    public CashRegister findByEmployeeId(String employeeId) throws NotFoundException {
        return cashRegisterRepository.findByEmployeeId(employeeId)
                .orElseThrow(
                        () -> new NotFoundException("No se encontró el registro de caja con empleado: " + employeeId));
    }

    @Override
    public CashRegister changeChasRegisterToEmployee(String cashRegisterId, String employeeId)
            throws NotFoundException {
        // Busacamos el cash register que queremos asignar al empleado
        CashRegister cashRegister = findById(cashRegisterId);
        // Buscamos el que empleado ya tiene asignado un cash register
        CashRegister cashRegisterEmployee = findByEmployeeId(employeeId);
        if (cashRegisterEmployee != null) {
            // Si el empleado ya tiene un cash register asignado, lo desasignamos
            cashRegisterEmployee.setEmployeeId(null);
            cashRegisterRepository.save(cashRegisterEmployee);
        }
        // Asignamos el cash register al empleado
        cashRegister.setEmployeeId(employeeId);
        cashRegister = cashRegisterRepository.save(cashRegister);
        return cashRegister;
    }
}
