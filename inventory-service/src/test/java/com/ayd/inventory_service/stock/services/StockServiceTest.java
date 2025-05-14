package com.ayd.inventory_service.stock.services;

import com.ayd.inventory_service.stock.models.Stock;
import com.ayd.inventory_service.stock.repositories.StockRepository;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private static final String PRODUCT_ID = "product-123";
    private static final String WAREHOUSE_ID = "warehouse-123";
    private static final int INITIAL_QUANTITY = 10;
    private static final int INITIAL_MINIMUM_STOCK = 2;

    private Warehouse warehouse;
    private Stock stock;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(WAREHOUSE_ID);

        stock = new Stock(PRODUCT_ID, INITIAL_QUANTITY, INITIAL_MINIMUM_STOCK, warehouse);
        stock.setId("stock-123");
    }

    /**
     * dado: un almacén válido.
     * cuando: se invoca getProductsLowStockByWarehouseId.
     * entonces: retorna la lista de productos con bajo stock asociados al almacén.
     */
    @Test
    void getProductsLowStockByWarehouseId_shouldReturnStocksWhenWarehouseIsValid() {
        // Arrange
        when(stockRepository.findProductsLowStockByWarehouseId(warehouse.getId())).thenReturn(List.of(stock));

        // Act
        List<Stock> result = stockService.getProductsLowStockByWarehouseId(warehouse);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(warehouse.getId(), result.get(0).getWarehouse().getId());
        verify(stockRepository).findProductsLowStockByWarehouseId(warehouse.getId());
    }

    /**
     * dado: que existen productos con bajo stock en general.
     * cuando: se llama a getProductsLowStock.
     * entonces: se retorna la lista de dichos productos correctamente.
     */
    @Test
    void getProductsLowStock_shouldReturnLowStockProducts() {
        // Arrange
        when(stockRepository.findProductsLowStock()).thenReturn(List.of(stock));

        // Act
        List<Stock> result = stockService.getProductsLowStock();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockRepository).findProductsLowStock();
    }

    /**
     * dado: que existen registros de stock para el producto.
     * cuando: se llama a getProductById con un ID válido.
     * entonces: se devuelve la lista correspondiente de registros de stock.
     */
    @Test
    void getProductById_shouldReturnStockList_whenProductExists() throws NotFoundException {
        // Arrange
        when(stockRepository.findByProductId(PRODUCT_ID)).thenReturn(List.of(stock));

        // Act
        List<Stock> result = stockService.getProductById(PRODUCT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockRepository).findByProductId(PRODUCT_ID);
    }

    /**
     * dado: que no existen registros de stock para el producto.
     * cuando: se llama a getProductById con un ID inexistente.
     * entonces: se lanza una NotFoundException.
     */
    @Test
    void getProductById_shouldThrowNotFoundException_whenProductDoesNotExist() {
        // Arrange
        when(stockRepository.findByProductId(PRODUCT_ID)).thenReturn(List.of());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> stockService.getProductById(PRODUCT_ID));
        verify(stockRepository).findByProductId(PRODUCT_ID);
    }

    /**
     * dado: que ya existe un registro de stock para el producto y almacén.
     * cuando: se agrega una cantidad positiva de stock.
     * entonces: se suma la cantidad al stock existente y se guarda.
     */
    @Test
    void addStock_shouldAddQuantity_whenStockExists() throws NotFoundException {
        // Arrange
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId())).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId()))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // Act
        Stock result = stockService.addStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, 10);

        // Assert
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        verify(stockRepository).existsByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId());
        verify(stockRepository).findByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId());
        verify(stockRepository).save(any(Stock.class));
    }

    
    /**
     * dado: que se proporciona una cantidad negativa.
     * cuando: se intenta agregar stock.
     * entonces: se lanza IllegalStateException y no se guarda el registro.
     */
    @Test
    void addStock_shouldThrowException_whenQuantityIsNegative() {
        // Arrange
        int negativeQuantity = -5;

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> stockService.addStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, negativeQuantity));
    }

    /**
     * dado: que ya existe un registro de stock con suficiente cantidad.
     * cuando: se solicita remover una cantidad válida.
     * entonces: se descuenta la cantidad del stock y se guarda correctamente.
     */
    @Test
    void removeStock_shouldSubtractQuantity_whenValidRequest() throws NotFoundException {
        // Arrange
        stock.setQuantity(20);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId())).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId()))
                .thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // Act
        Stock result = stockService.removeStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, 10);

        // Assert
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        verify(stockRepository).save(any(Stock.class));
    }



    /**
     * dado: que se proporciona una cantidad negativa.
     * cuando: se intenta remover stock.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void removeStock_shouldThrow_whenQuantityIsNegative() {
        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> stockService.removeStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, -1));
    }

    /**
     * dado: que la cantidad a remover es mayor a la cantidad existente.
     * cuando: se intenta remover stock.
     * entonces: se lanza IllegalStateException.
     */
    @Test
    void removeStock_shouldThrow_whenQuantityGreaterThanAvailable() {
        // Arrange
        stock.setQuantity(5);
        when(stockRepository.existsByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId())).thenReturn(true);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, warehouse.getId()))
                .thenReturn(Optional.of(stock));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> stockService.removeStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, 10));
    }

    /**
     * dado: que se proporciona un ID de producto, almacén, cantidad válida y stock
     * mínimo.
     * cuando: se llama al método createStock.
     * entonces: se crea y guarda correctamente el registro de stock.
     */
    @Test
    void createStock_shouldSaveStockSuccessfully_whenValidData() throws NotFoundException {
        // Arrange
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // Act
        Stock result = stockService.createStock(PRODUCT_ID, warehouse, 10, 5);

        // Assert
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.getProductId());
        verify(stockRepository).save(any(Stock.class));
    }

    /**
     * dado: que se proporciona una cantidad negativa.
     * cuando: se llama al método createStock.
     * entonces: se lanza una IllegalStateException.
     */
    @Test
    void createStock_shouldThrow_whenQuantityIsNegative() {
        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> stockService.createStock(PRODUCT_ID, warehouse, -1, 5));
    }

    /**
     * dado: que existe un stock para el producto y almacén, y se proporciona un
     * mínimo válido.
     * cuando: se llama al método updateMinumumStockByProductIdAndWarehouseId.
     * entonces: se actualiza el mínimo de stock y se guarda correctamente.
     */
    @Test
    void updateMinumumStock_shouldUpdateSuccessfully_whenValidMinimumStock() throws NotFoundException {
        // Arrange
        stock.setMinimumStock(5);
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // Act
        Stock result = stockService.updateMinumumStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, 10);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getMinimumStock());
        verify(stockRepository).findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID);
        verify(stockRepository).save(stock);
    }

    /**
     * dado: que no existe un stock para el producto y almacén.
     * cuando: se llama al método updateMinumumStockByProductIdAndWarehouseId.
     * entonces: se lanza una NotFoundException.
     */
    @Test
    void updateMinumumStock_shouldThrowNotFoundException_whenStockNotFound() {
        // Arrange
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> stockService.updateMinumumStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, 10));
        verify(stockRepository).findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID);
    }

    /**
     * dado: que se proporciona un stock mínimo negativo.
     * cuando: se llama al método updateMinumumStockByProductIdAndWarehouseId.
     * entonces: se lanza una IllegalStateException.
     */
    @Test
    void updateMinumumStock_shouldThrow_whenMinimumStockIsNegative() {
        // Arrange
        when(stockRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(Optional.of(stock));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> stockService.updateMinumumStockByProductIdAndWarehouseId(PRODUCT_ID, warehouse, -1));
    }

    /**
     * dado: que existe un almacén con stocks asociados.
     * cuando: se llama al método getProductsByWarehouseId.
     * entonces: se retorna la lista de stocks correspondientes al almacén.
     */
    @Test
    void getProductsByWarehouseId_shouldReturnStockList_whenWarehouseExists() {
        // Arrange
        List<Stock> expectedStocks = List.of(stock);
        when(stockRepository.findByWarehouseId(WAREHOUSE_ID)).thenReturn(expectedStocks);

        // Act
        List<Stock> result = stockService.getProductsByWarehouseId(warehouse);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stock.getProductId(), result.get(0).getProductId());
        verify(stockRepository).findByWarehouseId(WAREHOUSE_ID);
    }

}
