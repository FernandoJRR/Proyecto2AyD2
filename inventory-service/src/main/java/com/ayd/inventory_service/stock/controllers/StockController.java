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

    @Operation(summary = "Obtener productos por bodega", description = "Devuelve una lista de productos almacenados en una bodega específica, identificada por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/warehouse/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsByWarehouse(@PathVariable String warehouseId)
            throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(warehouseId);
        List<Stock> stocks = forStockPort.getProductsByWarehouseId(warehouse);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @Operation(summary = "Obtener productos con bajo stock", description = "Devuelve una lista de productos cuyo stock actual está por debajo del mínimo definido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos con bajo stock obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/low-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsLowStock() {
        List<Stock> stocks = forStockPort.getProductsLowStock();
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @Operation(summary = "Obtener productos con bajo stock por bodega", description = "Devuelve una lista de productos cuyo stock está por debajo del mínimo permitido dentro de una bodega específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos con bajo stock obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/low-stock/warehouse/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> getProductsLowStockByWarehouse(@PathVariable String warehouseId)
            throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(warehouseId);
        List<Stock> stocks = forStockPort.getProductsLowStockByWarehouseId(warehouse);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

    @Operation(summary = "Actualizar stock mínimo de un producto en una bodega", description = "Permite modificar el valor mínimo de stock permitido para un producto dentro de una bodega específica. Requiere el ID del producto y el ID de la bodega.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock mínimo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Producto o bodega no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })

    @PatchMapping("/minimum-stock")
    @ResponseStatus(HttpStatus.OK)
    public StockResponseDTO updateMinumumStockByProductIdAndWarehouseId(
            @RequestBody @Valid UpdateMinStockRequestDTO updateMinStockRequestDTO) throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(updateMinStockRequestDTO.getWarehouseId());
        Stock stock = forStockPort.updateMinumumStockByProductIdAndWarehouseId(updateMinStockRequestDTO.getProductId(),
                warehouse, updateMinStockRequestDTO.getMinimumStock());
        return stockMapper.fromStockToStockResponseDTO(stock);
    }

    @Operation(summary = "Modificar el stock de varios productos", description = "Permite restar stock a varios productos en distintas bodegas. Cada elemento de la lista debe contener el ID del producto, el ID de la bodega y la cantidad a restar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente para todos los productos"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Algún producto o bodega no fue encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto en la operación de stock (por ejemplo, stock insuficiente)"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/modify-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponseDTO> substractVariousStockByProductIdAndWarehouseId(
            @RequestBody @Valid List<ModifyStockRequest> modifyStockRequest)
            throws NotFoundException, IllegalStateException {
        List<Stock> stocks = forModifiyStockPort.substractVariousStockByProductIdAndWarehouseId(modifyStockRequest);
        return stockMapper.fromStockListToStockResponseDTOList(stocks);
    }

}
