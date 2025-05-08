package com.ayd.inventory_service.stock.repositories;

import com.ayd.inventory_service.stock.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, String> {
    boolean existsByProductIdAndWarehouseId(String productId, String warehouseId);

    Optional<Stock> findByProductIdAndWarehouseId(String productId, String warehouseId);

    List<Stock> findByWarehouseId(String warehouseId);
    List<Stock> findByProductId(String productId);

    @Query(nativeQuery = true, value = "SELECT * FROM stock s WHERE s.quantity <= s.minimum_stock AND s.warehouse_id = ?1")
    List<Stock> findProductsLowStockByWarehouseId(String warehouseId);

    @Query(nativeQuery = true, value = "SELECT * FROM stock s WHERE s.quantity <= s.minimum_stock")
    List<Stock> findProductsLowStock();
}
