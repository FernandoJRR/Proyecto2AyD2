package com.ayd.inventory_service.supplier.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.supplier.dtos.SupplierResponseDTO;
import com.ayd.inventory_service.supplier.models.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    public SupplierResponseDTO fromSupplierToSupplierResponseDTO(Supplier supplier);

    public List<SupplierResponseDTO> fromSupplierListToSupplierResponseDTOList(List<Supplier> supplierList);
}
