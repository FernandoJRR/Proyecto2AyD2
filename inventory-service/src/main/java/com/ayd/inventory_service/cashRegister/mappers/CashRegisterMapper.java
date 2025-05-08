package com.ayd.inventory_service.cashRegister.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.cashRegister.dtos.CashRegisterResponseDTO;
import com.ayd.inventory_service.cashRegister.models.CashRegister;

@Mapper(componentModel = "spring")
public interface CashRegisterMapper {
    public CashRegisterResponseDTO fromCashRegisterToCashRegisterResponseDTO(CashRegister cashRegister);
    public List<CashRegisterResponseDTO> fromCashRegisterListToCashRegisterResponseDTOList(List<CashRegister> cashRegisterList);
}
