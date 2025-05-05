package com.ayd.inventory_service.warehouse.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.warehouse.dtos.CreateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.SpecificationWarehouseDTO;
import com.ayd.inventory_service.warehouse.dtos.UpdateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.repositories.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {
    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    private static final String WAREHOUSE_ID = "wh-123";
    private static final String NAME = "Bodega Central";
    private static final String UBICATION = "Zona 1, Quetzaltenango";
    private static final boolean ACTIVE = true;

    private static final String UPDATED_NAME = "Bodega Norte";
    private static final String UPDATED_UBICATION = "Zona 5, Quetzaltenango";
    private static final boolean UPDATED_ACTIVE = false;

    private CreateWarehouseRequestDTO createWarehouseRequestDTO;
    private UpdateWarehouseRequestDTO updateWarehouseRequestDTO;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        createWarehouseRequestDTO = new CreateWarehouseRequestDTO(NAME, UBICATION, ACTIVE);
        updateWarehouseRequestDTO = new UpdateWarehouseRequestDTO(UPDATED_NAME, UPDATED_UBICATION, UPDATED_ACTIVE);
        warehouse = new Warehouse(NAME, UBICATION, ACTIVE, null, null);
        warehouse.setId(WAREHOUSE_ID);
    }

    /**
     * dado: que no existe una bodega con el mismo nombre.
     * cuando: se llama al método createWarehouse.
     * entonces: se guarda la bodega correctamente y se retorna la instancia creada.
     */
    @Test
    public void createWarehouseShouldCreateSuccessfullyWhenNoDuplicateExists() throws DuplicatedEntryException {
        // Arrange
        when(warehouseRepository.existsByName(NAME)).thenReturn(false);
        when(warehouseRepository.save(any(Warehouse.class))).thenAnswer(invocation -> {
            Warehouse saved = invocation.getArgument(0);
            saved.setId(WAREHOUSE_ID);
            return saved;
        });

        // Act
        Warehouse result = warehouseService.createWarehouse(createWarehouseRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(WAREHOUSE_ID, result.getId()),
                () -> assertEquals(NAME, result.getName()),
                () -> assertEquals(UBICATION, result.getUbication()),
                () -> assertEquals(ACTIVE, result.isActive()));

        verify(warehouseRepository).existsByName(NAME);
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    /**
     * dado: que ya existe una bodega con el mismo nombre.
     * cuando: se llama al método createWarehouse.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guarda la
     * bodega.
     */
    @Test
    public void createWarehouseShouldThrowWhenNameAlreadyExists() {
        // Arrange
        when(warehouseRepository.existsByName(NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> warehouseService.createWarehouse(createWarehouseRequestDTO));

        verify(warehouseRepository).existsByName(NAME);
        verify(warehouseRepository, never()).save(any());
    }

    /**
     * dado: que existe una bodega con el ID proporcionado y no hay duplicado.
     * cuando: se llama al método updateWarehouse.
     * entonces: se actualiza y se guarda correctamente.
     */
    @Test
    public void updateWarehouseShouldUpdateSuccessfullyWhenNoDuplicates()
            throws DuplicatedEntryException, NotFoundException {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.existsByNameAndIdNot(WAREHOUSE_ID, UPDATED_NAME)).thenReturn(false);
        when(warehouseRepository.save(any(Warehouse.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Warehouse result = warehouseService.updateWarehouse(WAREHOUSE_ID, updateWarehouseRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(UPDATED_NAME, result.getName()),
                () -> assertEquals(UPDATED_UBICATION, result.getUbication()),
                () -> assertEquals(UPDATED_ACTIVE, result.isActive()));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
        verify(warehouseRepository).existsByNameAndIdNot(WAREHOUSE_ID, UPDATED_NAME);
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    /**
     * dado: que no existe una bodega con el ID proporcionado.
     * cuando: se llama al método updateWarehouse.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void updateWarehouseShouldThrowWhenWarehouseNotFound() {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> warehouseService.updateWarehouse(WAREHOUSE_ID, updateWarehouseRequestDTO));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
        verify(warehouseRepository, never()).save(any());
    }

    /**
     * dado: que ya existe otra bodega con el mismo nombre.
     * cuando: se llama al método updateWarehouse.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void updateWarehouseShouldThrowWhenNameAlreadyExistsInOtherWarehouse() {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.existsByNameAndIdNot(WAREHOUSE_ID, UPDATED_NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> warehouseService.updateWarehouse(WAREHOUSE_ID, updateWarehouseRequestDTO));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
        verify(warehouseRepository).existsByNameAndIdNot(WAREHOUSE_ID, UPDATED_NAME);
        verify(warehouseRepository, never()).save(any());
    }

    /**
     * dado: que existe una bodega con el ID proporcionado.
     * cuando: se llama al método deleteWarehouse.
     * entonces: se elimina correctamente y se retorna true.
     */
    @Test
    public void deleteWarehouseShouldDeleteSuccessfullyWhenWarehouseExists() throws NotFoundException {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.of(warehouse));

        // Act
        boolean result = warehouseService.deleteWarehouse(WAREHOUSE_ID);

        // Assert
        assertTrue(result);
        verify(warehouseRepository).findById(WAREHOUSE_ID);
        verify(warehouseRepository).delete(warehouse);
    }

    /**
     * dado: que no existe una bodega con el ID proporcionado.
     * cuando: se llama al método deleteWarehouse.
     * entonces: se lanza NotFoundException y no se intenta eliminar.
     */
    @Test
    public void deleteWarehouseShouldThrowNotFoundExceptionWhenWarehouseDoesNotExist() {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> warehouseService.deleteWarehouse(WAREHOUSE_ID));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
        verify(warehouseRepository, never()).delete((Warehouse) any());
    }

    /**
     * dado: que existe una bodega con el ID proporcionado.
     * cuando: se llama al método getWarehouse.
     * entonces: se retorna correctamente la bodega encontrada.
     */
    @Test
    public void getWarehouseShouldReturnWarehouseWhenExists() throws NotFoundException {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.of(warehouse));

        // Act
        Warehouse result = warehouseService.getWarehouse(WAREHOUSE_ID);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(NAME, result.getName()),
                () -> assertEquals(UBICATION, result.getUbication()),
                () -> assertEquals(ACTIVE, result.isActive()));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
    }

    /**
     * dado: que no existe una bodega con el ID proporcionado.
     * cuando: se llama al método getWarehouse.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void getWarehouseShouldThrowNotFoundExceptionWhenWarehouseDoesNotExist() {
        // Arrange
        when(warehouseRepository.findById(WAREHOUSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> warehouseService.getWarehouse(WAREHOUSE_ID));

        verify(warehouseRepository).findById(WAREHOUSE_ID);
    }

    /**
     * dado: que el parámetro specificationWarehouseDTO es null.
     * cuando: se llama al método getWarehouses.
     * entonces: se retorna la lista completa de bodegas.
     */
    @Test
    public void getWarehousesShouldReturnAllWhenSpecificationIsNull() {
        // Arrange
        List<Warehouse> mockWarehouses = List.of(warehouse);
        when(warehouseRepository.findAll()).thenReturn(mockWarehouses);

        // Act
        List<Warehouse> result = warehouseService.getWarehouses(null);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(NAME, result.get(0).getName()));

        verify(warehouseRepository).findAll();
    }

    /**
     * dado: que specificationWarehouseDTO contiene filtros.
     * cuando: se llama al método getWarehouses.
     * entonces: se aplica la Specification y se retorna la lista filtrada.
     */
    @Test
    public void getWarehousesShouldReturnFilteredResultsWhenSpecificationProvided() {
        // Arrange
        SpecificationWarehouseDTO dto = new SpecificationWarehouseDTO(NAME, UBICATION,
                ACTIVE);
        List<Warehouse> filteredWarehouses = List.of(warehouse);

        when(warehouseRepository.findAll(any(Specification.class))).thenReturn(filteredWarehouses);

        // Act
        List<Warehouse> result = warehouseService.getWarehouses(dto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(NAME, result.get(0).getName()));

        verify(warehouseRepository).findAll(any(Specification.class));
    }

}
