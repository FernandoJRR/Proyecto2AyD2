package com.ayd.inventory_service.warehouse.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.warehouse.dtos.WarehouseResponseDTO;
import com.ayd.inventory_service.warehouse.models.Warehouse;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    public WarehouseResponseDTO fromWarehouseToWarehouseResponseDTO(Warehouse warehouse);

    public List<WarehouseResponseDTO> fromWarehouseListToWarehouseResponseDTOList(List<Warehouse> warehouseList);
}
