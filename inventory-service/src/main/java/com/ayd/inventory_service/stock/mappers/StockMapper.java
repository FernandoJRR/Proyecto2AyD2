package com.ayd.inventory_service.stock.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.inventory_service.stock.dtos.StockResponseDTO;
import com.ayd.inventory_service.stock.models.Stock;

@Mapper(componentModel = "spring")
public interface StockMapper {
    public StockResponseDTO fromStockToStockResponseDTO(Stock stock);

    public List<StockResponseDTO> fromStockListToStockResponseDTOList(List<Stock> stockList);
}
