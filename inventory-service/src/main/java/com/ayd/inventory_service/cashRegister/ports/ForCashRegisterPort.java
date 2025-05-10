package com.ayd.inventory_service.cashRegister.ports;

import java.util.List;

import com.ayd.inventory_service.cashRegister.dtos.CreateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.SpecificationCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.UpdateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;

public interface ForCashRegisterPort {
    public List<CashRegister> findAll();

    public List<CashRegister> findAllBySpecification(
            SpecificationCashRegisterRequestDTO specificationCashRegisterRequestDTO);

    public CashRegister findById(String id) throws NotFoundException;

    public CashRegister findByCode(String code) throws NotFoundException;

    public CashRegister save(CreateCashRegisterRequestDTO createCashRegisterRequestDTO) throws DuplicatedEntryException,NotFoundException;

    public CashRegister update(String id, UpdateCashRegisterRequestDTO updateCashRegisterRequestDTO)
            throws NotFoundException,
            DuplicatedEntryException;

    public CashRegister toggleActive(String id) throws NotFoundException;

    public CashRegister findByEmployeeId(String employeeId) throws NotFoundException;
}
