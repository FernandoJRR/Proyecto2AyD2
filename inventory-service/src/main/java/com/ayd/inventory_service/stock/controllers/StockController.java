package com.ayd.inventory_service.stock.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.dtos.ModifyStockRequest;
import com.ayd.inventory_service.stock.dtos.StockResponseDTO;
import com.ayd.inventory_service.stock.dtos.UpdateMinStockRequestDTO;
import com.ayd.inventory_service.stock.mappers.StockMapper;
import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.stock.ports.ForModifiyStockPort;
import com.ayd.inventory_service.stock.ports.ForStockPort;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final ForStockPort forStockPort;
    private final ForModifiyStockPort forModifiyStockPort;
    private final ForWarehousePort forWarehousePort;
    private final StockMapper stockMapper;

    @GetMapping("/warehouse/{warehouseId}/products")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsByWarehouse(@PathVariable String warehouseId)
            throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(warehouseId);
        List<Stock> stocks = forStockPort.getProductsByWarehouseId(warehouse);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @GetMapping("/products/low-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsLowStock() {
        List<Stock> stocks = forStockPort.getProductsLowStock();
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @GetMapping("/products/low-stock/warehouse/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsLowStockByWarehouse(@PathVariable String warehouseId)
            throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(warehouseId);
        List<Stock> stocks = forStockPort.getProductsLowStockByWarehouseId(warehouse);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @PatchMapping("/products/minimum-stock")
    @ResponseStatus(HttpStatus.OK)
    public StockResponseDTO updateMinumumStockByProductIdAndWarehouseId(
            @RequestBody @Valid UpdateMinStockRequestDTO updateMinStockRequestDTO) throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(updateMinStockRequestDTO.getWarehouseId());
        Stock stock = forStockPort.updateMinumumStockByProductIdAndWarehouseId(updateMinStockRequestDTO.getProductId(),
                warehouse, updateMinStockRequestDTO.getMinimumStock());
        return stockMapper.fromStockToStockResponseDTO(stock);
    }

    @PostMapping("/products/modify")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> substractVariousStockByProductIdAndWarehouseId(
            @RequestBody @Valid List<ModifyStockRequest> modifyStockRequest)
            throws NotFoundException, IllegalStateException {
        List<Stock> stocks = forModifiyStockPort.substractVariousStockByProductIdAndWarehouseId(modifyStockRequest);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

}
