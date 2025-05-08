package com.ayd.inventory_service.productEntries.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailResponseDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;


@Mapper(componentModel = "spring")
public interface ProductEntryDetailMapper {
    public ProductEntryDetailResponseDTO fromProductEntryDetailToProductEntryDetailResponseDTO(ProductEntryDetail productEntryDetail);
    public List<ProductEntryDetailResponseDTO> fromProductEntryDetailListToProductEntryDetailResponseDTOList(List<ProductEntryDetail> productEntryDetailList);
}
