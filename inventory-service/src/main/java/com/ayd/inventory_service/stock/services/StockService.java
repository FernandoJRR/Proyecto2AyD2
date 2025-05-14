package com.ayd.inventory_service.stock.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.stock.ports.ForStockPort;
import com.ayd.inventory_service.stock.repositories.StockRepository;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class StockService implements ForStockPort {
    private final StockRepository stockRepository;

    @Override
    public List<Stock> getProductsLowStockByWarehouseId(Warehouse warehouse){
        List<Stock> stocks = stockRepository.findProductsLowStockByWarehouseId(warehouse.getId());
        return stocks;
    }

    @Override
    public List<Stock> getProductsLowStock() {
        List<Stock> stocks = stockRepository.findProductsLowStock();
        return stocks;
    }

    @Override
    public List<Stock> getProductById(String productId) throws NotFoundException {
        List<Stock> stocks = stockRepository.findByProductId(productId);
        if (stocks.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        return stocks;
    }

    @Override
    public Stock addStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer quantity)
            throws NotFoundException, IllegalStateException {
        Stock stock = null;
        if (stockRepository.existsByProductIdAndWarehouseId(productId, warehouse.getId())) {
            stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouse.getId())
                    .orElseThrow(() -> new NotFoundException("Stock no encontrado"));
        } else {
            stock = createStock(productId, warehouse, 0, 0);
        }
        // Validamos que la cantidad no sea negativa
        if (quantity < 0) {
            throw new IllegalStateException("La cantidad no puede ser negativa");
        }
        stock.sumQuantity(quantity);
        stock = stockRepository.save(stock);
        return stock;
    }

    @Override
    public Stock removeStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer quantity)
            throws NotFoundException, IllegalStateException {
        Stock stock = null;
        if (stockRepository.existsByProductIdAndWarehouseId(productId, warehouse.getId())) {
            stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouse.getId())
                    .orElseThrow(() -> new NotFoundException("Stock no encontrado"));
        } else {
            stock = createStock(productId, warehouse, 0, 0);
        }
        // Validamos que la cantidad no sea negativa
        if (quantity < 0) {
            throw new IllegalStateException("La cantidad no puede ser negativa");
        }
        // Validamos que la cantidad no sea mayor a la cantidad actual
        if (quantity > stock.getQuantity()) {
            throw new IllegalStateException("La cantidad no puede ser mayor a la cantidad actual");
        }
        stock.subQuantity(quantity);
        stock = stockRepository.save(stock);
        return stock;
    }

    @Override
    public Stock createStock(String productId, Warehouse warehouse, Integer quantity, Integer minimumStock)
            throws NotFoundException, IllegalStateException {
        if (quantity < 0) {
            throw new IllegalStateException("La cantidad no puede ser negativa");
        }
        Stock stock = new Stock(productId, quantity, 0, warehouse);
        stock = stockRepository.save(stock);
        return stock;
    }

    @Override
    public Stock updateMinumumStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer minimumStock)
            throws NotFoundException, IllegalStateException {
        Stock stock = stockRepository.findByProductIdAndWarehouseId(productId, warehouse.getId())
                .orElseThrow(() -> new NotFoundException("Stock no encontrado"));
        if (minimumStock < 0) {
            throw new IllegalStateException("La cantidad no puede ser negativa");
        }
        stock.setMinimumStock(minimumStock);
        stock = stockRepository.save(stock);
        return stock;
    }

    @Override
    public List<Stock> getProductsByWarehouseId(Warehouse warehouse) {
        List<Stock> stocks = stockRepository.findByWarehouseId(warehouse.getId());
        return stocks;
    }

}
