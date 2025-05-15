package com.ayd.inventory_service.stock.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.shared.exceptions.*;
import com.ayd.sharedInventoryService.stock.dto.ModifyStockRequest;
import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.stock.ports.ForModifiyStockPort;
import com.ayd.inventory_service.stock.repositories.StockRepository;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class ModifyStockService implements ForModifiyStockPort {

    private final StockRepository stockRepository;
    private final ForWarehousePort forWarehousePort;

    @Override
    public Stock substractStockByProductIdAndWarehouseId(String productId, String warehouseId, Integer quantity)
            throws NotFoundException, IllegalStateException {
        Warehouse warehouse = forWarehousePort.getWarehouse(warehouseId);
        if (!stockRepository.existsByProductIdAndWarehouseId(productId, warehouse.getId())) {
            throw new NotFoundException("El producto no existe en el almacen");
        }
        Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouse.getId())
                .orElseThrow(() -> new NotFoundException("Stock no encontrado"));
        // Validamos que la cantidad no sea negativa
        if (quantity < 0) {
            throw new IllegalStateException("La cantidad no puede ser negativa");
        }
        // Validamos que la cantidad no sea mayor a la cantidad en stock
        if (quantity > stock.getQuantity()) {
            throw new IllegalStateException("La cantidad a restar es mayor a la cantidad en stock");
        }
        stock.subQuantity(quantity);
        stock = stockRepository.save(stock);
        return stock;
    }

    @Override
    public List<Stock> substractVariousStockByProductIdAndWarehouseId(List<ModifyStockRequest> modifyStockRequest)
            throws NotFoundException, IllegalStateException {
        List<Stock> stocks = new ArrayList<>();
        for (ModifyStockRequest request : modifyStockRequest) {
            stocks.add(substractStockByProductIdAndWarehouseId(request.getProductId(), request.getWarehouseId(),
                    request.getQuantity()));
        }
        return stocks;
    }

}
