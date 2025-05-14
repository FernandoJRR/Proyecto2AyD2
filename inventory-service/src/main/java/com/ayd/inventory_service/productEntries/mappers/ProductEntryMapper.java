package com.ayd.inventory_service.productEntries.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryResponseDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;

@Mapper(componentModel = "spring")
public interface ProductEntryMapper {
    public ProductEntryResponseDTO formProductEntryToProductEntryResponseDTO(ProductEntry productEntry);
    public List<ProductEntryResponseDTO> formProductEntryListToProductEntryResponseDTOList(List<ProductEntry> productEntryList);
}
