package com.ayd.inventory_service.cashRegister.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.sharedInventoryService.cashRegister.dto.CashRegisterResponseDTO;

@Mapper(componentModel = "spring")
public interface CashRegisterMapper {
    public CashRegisterResponseDTO fromCashRegisterToCashRegisterResponseDTO(CashRegister cashRegister);
    public List<CashRegisterResponseDTO> fromCashRegisterListToCashRegisterResponseDTOList(List<CashRegister> cashRegisterList);
}
