package com.ayd.inventory_service.stock.ports;

import java.util.List;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.warehouse.models.Warehouse;

public interface ForStockPort {
    public List<Stock> getProductsLowStockByWarehouseId(Warehouse warehouse);

    public List<Stock> getProductsLowStock();

    public List<Stock> getProductById(String productId)
            throws NotFoundException;

    public Stock addStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer quantity)
            throws NotFoundException, IllegalStateException;

    public Stock removeStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer quantity)
            throws NotFoundException, IllegalStateException;

    public Stock createStock(String productId, Warehouse warehouse, Integer quantity,Integer minimumStock)
            throws NotFoundException, IllegalStateException, DuplicatedEntryException;

    public Stock updateMinumumStockByProductIdAndWarehouseId(String productId, Warehouse warehouse, Integer minimumStock)
            throws NotFoundException, IllegalStateException;
}
