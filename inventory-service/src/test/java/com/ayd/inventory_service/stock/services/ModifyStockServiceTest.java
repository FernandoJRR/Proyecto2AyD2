package com.ayd.inventory_service.stock.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.dtos.ModifyStockRequest;
import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.stock.repositories.StockRepository;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

@ExtendWith(MockitoExtension.class)
class ModifyStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ForWarehousePort forWarehousePort;

    @InjectMocks
    private ModifyStockService modifyStockService;

    private static final String PRODUCT_ID = "product-001";
    private static final String WAREHOUSE_ID = "warehouse-001";
    private static final Integer INITIAL_QUANTITY = 50;
    private static final Integer QUANTITY_TO_SUBTRACT = 10;

    private Warehouse warehouse;
    private Stock stock;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(WAREHOUSE_ID);

        stock = new Stock();
        stock.setProductId(PRODUCT_ID);
        stock.setWarehouse(warehouse);
        stock.setQuantity(INITIAL_QUANTITY);
        stock.setMinimumStock(5);
    }

    /**
     * dado: el producto existe en el almacén y tiene suficiente stock.
     * cuando: se solicita restar una cantidad válida de stock.
     * entonces: se actualiza el stock y se retorna correctamente.
     */
    @Test
    void substractStockByProductIdAndWarehouseId_shouldSucceed() throws NotFoundException {
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenAnswer(inv -> inv.getArgument(0));

        Stock result = modifyStockService.substractStockByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID,
                QUANTITY_TO_SUBTRACT);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(INITIAL_QUANTITY - QUANTITY_TO_SUBTRACT, result.getQuantity()));
    }

    /**
     * dado: el producto no existe en el almacén.
     * cuando: se intenta restar stock.
     * entonces: se lanza una excepción NotFoundException.
     * 
     * @throws NotFoundException
     */
    @Test
    void substractStock_shouldThrowWhenStockDoesNotExist() throws NotFoundException {
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> modifyStockService
                .substractStockByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID, QUANTITY_TO_SUBTRACT));
    }

    /**
     * dado: se solicita restar una cantidad negativa.
     * cuando: se llama al método.
     * entonces: se lanza una excepción IllegalStateException.
     * 
     * @throws NotFoundException
     */
    @Test
    void substractStock_shouldThrowWhenQuantityNegative() throws NotFoundException {
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.of(stock));

        assertThrows(IllegalStateException.class,
                () -> modifyStockService.substractStockByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID, -5));
    }

    /**
     * dado: se intenta restar más stock del disponible.
     * cuando: se llama al método.
     * entonces: se lanza una excepción IllegalStateException.
     * 
     * @throws NotFoundException
     */
    @Test
    void substractStock_shouldThrowWhenQuantityExceedsStock() throws NotFoundException {
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.of(stock));

        assertThrows(IllegalStateException.class, () -> modifyStockService
                .substractStockByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID, INITIAL_QUANTITY + 1));
    }

    /**
     * dado: que una de las solicitudes tiene cantidad mayor al stock.
     * cuando: se llama al método substractVariousStockByProductIdAndWarehouseId.
     * entonces: se lanza una IllegalStateException y no se completan las
     * operaciones.
     */
    @Test
    void substractVariousStock_ShouldThrow_WhenQuantityExceedsStock() throws NotFoundException {
        // Arrange
        ModifyStockRequest request = new ModifyStockRequest(PRODUCT_ID, 200, WAREHOUSE_ID);
        List<ModifyStockRequest> requests = List.of(request);

        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID))
                .thenReturn(Optional.of(stock));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> modifyStockService.substractVariousStockByProductIdAndWarehouseId(requests));

        verify(stockRepository, never()).save(any());
    }

}
