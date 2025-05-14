package com.ayd.inventory_service.supplier.services;

import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.SpecificationSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.UpdateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.repositories.SupplierRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private static final String SUPPLIER_ID = "supplier-123";
    private static final String NIT = "1234567-8";
    private static final String NAME = "Proveedor S.A.";
    private static final BigDecimal TAX_REGIME = new BigDecimal("12.00");
    private static final String ADDRESS = "Calle Principal Zona 1";
    private static final boolean ACTIVE = true;

    private static final String UPDATED_NIT = "7654321-0";
    private static final String UPDATED_NAME = "Proveedor Actualizado";
    private static final BigDecimal UPDATED_TAX_REGIME = new BigDecimal("5.00");
    private static final String UPDATED_ADDRESS = "Avenida Reforma Zona 10";
    private static final boolean UPDATED_ACTIVE = false;

    private CreateSupplierRequestDTO createSupplierRequestDTO;
    private UpdateSupplierRequestDTO updateSupplierRequestDTO;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        createSupplierRequestDTO = new CreateSupplierRequestDTO(NIT, NAME, TAX_REGIME, ADDRESS, ACTIVE);
        updateSupplierRequestDTO = new UpdateSupplierRequestDTO(UPDATED_NIT, UPDATED_NAME, UPDATED_TAX_REGIME,
                UPDATED_ADDRESS, UPDATED_ACTIVE);
        supplier = new Supplier(NIT, NAME, TAX_REGIME, ADDRESS, ACTIVE, null);
        supplier.setId(SUPPLIER_ID);
    }

    /**
     * dado: un ID válido.
     * cuando: se llama a getSupplierById.
     * entonces: se retorna el proveedor correspondiente.
     */
    @Test
    public void shouldReturnSupplierWhenIdExists() throws NotFoundException {
        // Arrange
        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.of(supplier));

        // Act
        Supplier result = supplierService.getSupplierById(SUPPLIER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(SUPPLIER_ID, result.getId());
        verify(supplierRepository).findById(SUPPLIER_ID);
    }

    /**
     * dado: un ID que no existe.
     * cuando: se llama a getSupplierById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> supplierService.getSupplierById(SUPPLIER_ID));

        assertEquals("El proveedor no existe", exception.getMessage());
        verify(supplierRepository).findById(SUPPLIER_ID);
    }

    /**
     * dado: un nombre válido.
     * cuando: se llama a getSupplierByName.
     * entonces: se retorna el proveedor correspondiente.
     */
    @Test
    public void shouldReturnSupplierWhenNameExists() throws NotFoundException {
        // Arrange
        when(supplierRepository.findByName(NAME)).thenReturn(Optional.of(supplier));

        // Act
        Supplier result = supplierService.getSupplierByName(NAME);

        // Assert
        assertNotNull(result);
        assertEquals(NAME, result.getName());
        verify(supplierRepository).findByName(NAME);
    }

    /**
     * dado: un nombre que no existe.
     * cuando: se llama a getSupplierByName.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void shouldThrowNotFoundExceptionWhenNameDoesNotExist() {
        // Arrange
        when(supplierRepository.findByName(NAME)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> supplierService.getSupplierByName(NAME));

        assertEquals("El proveedor no existe", exception.getMessage());
        verify(supplierRepository).findByName(NAME);
    }

    /**
     * dado: que existen proveedores en la base de datos.
     * cuando: se llama a getAllSuppliers.
     * entonces: se retorna la lista completa de proveedores.
     */
    @Test
    public void shouldReturnAllSuppliersWhenTheyExist() {
        // Arrange
        List<Supplier> expectedSuppliers = List.of(supplier);
        when(supplierRepository.findAll()).thenReturn(expectedSuppliers);

        // Act
        List<Supplier> result = supplierService.getAllSuppliers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(NAME, result.get(0).getName());
        verify(supplierRepository).findAll();
    }

    /**
     * dado: que no hay proveedores registrados.
     * cuando: se llama a getAllSuppliers.
     * entonces: se retorna una lista vacía.
     */
    @Test
    public void shouldReturnEmptyListWhenNoSuppliersExist() {
        // Arrange
        when(supplierRepository.findAll()).thenReturn(List.of());

        // Act
        List<Supplier> result = supplierService.getAllSuppliers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierRepository).findAll();
    }

    /**
     * dado: que el proveedor con el ID existe.
     * cuando: se actualiza la información con datos válidos.
     * entonces: se guarda y retorna el proveedor actualizado.
     */
    @Test
    public void shouldUpdateSupplierSuccessfullyWhenExists() throws NotFoundException {
        // Arrange
        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Supplier result = supplierService.updateSupplier(updateSupplierRequestDTO, SUPPLIER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(UPDATED_NAME, result.getName());
        assertEquals(UPDATED_NIT, result.getNit());
        assertEquals(UPDATED_TAX_REGIME, result.getTaxRegime());
        assertEquals(UPDATED_ADDRESS, result.getAddress());
        assertEquals(UPDATED_ACTIVE, result.isActive());

        verify(supplierRepository).findById(SUPPLIER_ID);
        verify(supplierRepository).save(any(Supplier.class));
    }

    /**
     * dado: que el proveedor con el ID no existe.
     * cuando: se intenta actualizar.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void shouldThrowWhenSupplierToUpdateDoesNotExist() {
        // Arrange
        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> supplierService.updateSupplier(updateSupplierRequestDTO, SUPPLIER_ID));

        verify(supplierRepository).findById(SUPPLIER_ID);
        verify(supplierRepository, never()).save(any());
    }

    /**
     * dado: que se proporciona un DTO de especificación con filtros.
     * cuando: se llama al método getSuppliersBySpecification.
     * entonces: se retorna la lista filtrada de proveedores.
     */
    @Test
    public void shouldReturnFilteredSuppliersWhenSpecificationIsProvided() {
        // Arrange
        SpecificationSupplierRequestDTO filterDTO = new SpecificationSupplierRequestDTO(
                NIT, NAME, TAX_REGIME, ADDRESS, ACTIVE);
        when(supplierRepository.findAll(any(Specification.class))).thenReturn(List.of(supplier));

        // Act
        List<Supplier> result = supplierService.getSuppliersBySpecification(filterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(NAME, result.get(0).getName());
        verify(supplierRepository).findAll(any(Specification.class));
    }

    /**
     * dado: que no se proporciona ningún DTO de especificación.
     * cuando: se llama al método getSuppliersBySpecification con null.
     * entonces: se retorna la lista completa de proveedores.
     */
    @Test
    public void shouldReturnAllSuppliersWhenSpecificationIsNull() {
        // Arrange
        when(supplierRepository.findAll()).thenReturn(List.of(supplier));

        // Act
        List<Supplier> result = supplierService.getSuppliersBySpecification(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(supplierRepository).findAll();
    }

    /**
     * dado: un proveedor con estado activo.
     * cuando: se llama al método toogleSupplierStatus.
     * entonces: se desactiva el proveedor y se guarda.
     */
    @Test
    public void shouldToggleSupplierStatusSuccessfully() throws NotFoundException {
        // Arrange
        Supplier activeSupplier = new Supplier(NIT, NAME, TAX_REGIME, ADDRESS, true,
                null);
        activeSupplier.setId(SUPPLIER_ID);

        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.of(activeSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Supplier result = supplierService.toogleSupplierStatus(SUPPLIER_ID);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(supplierRepository).findById(SUPPLIER_ID);
        verify(supplierRepository).save(activeSupplier);
    }

    /**
     * dado: que el proveedor no existe.
     * cuando: se llama al método toogleSupplierStatus.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void shouldThrowWhenTogglingStatusOfNonexistentSupplier() {
        // Arrange
        when(supplierRepository.findById(SUPPLIER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> supplierService.toogleSupplierStatus(SUPPLIER_ID));
        verify(supplierRepository).findById(SUPPLIER_ID);
        verify(supplierRepository, never()).save(any());
    }

    /**
     * dado: que no existe un proveedor con el mismo nombre ni NIT.
     * cuando: se llama a saveSupplier.
     * entonces: se guarda correctamente y se retorna el proveedor.
     */
    @Test
    public void shouldSaveSupplierSuccessfullyWhenNoDuplicateExists() throws DuplicatedEntryException {
        // Arrange
        when(supplierRepository.existsByName(NAME)).thenReturn(false);
        when(supplierRepository.existsByNit(NIT)).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Supplier result = supplierService.saveSupplier(createSupplierRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(NAME, result.getName());
        assertEquals(NIT, result.getNit());
        verify(supplierRepository).existsByName(NAME);
        verify(supplierRepository).existsByNit(NIT);
        verify(supplierRepository).save(any(Supplier.class));
    }

    /**
     * dado: que ya existe un proveedor con el mismo nombre.
     * cuando: se llama a saveSupplier.
     * entonces: se lanza DuplicatedEntryException y no se guarda.
     */
    @Test
    public void shouldThrowWhenSupplierNameAlreadyExists() {
        // Arrange
        when(supplierRepository.existsByName(NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> supplierService.saveSupplier(createSupplierRequestDTO));
        verify(supplierRepository).existsByName(NAME);
        verify(supplierRepository, never()).existsByNit(anyString());
        verify(supplierRepository, never()).save(any());
    }

    /**
     * dado: que ya existe un proveedor con el mismo NIT.
     * cuando: se llama a saveSupplier.
     * entonces: se lanza DuplicatedEntryException y no se guarda.
     */
    @Test
    public void shouldThrowWhenSupplierNitAlreadyExists() {
        // Arrange
        when(supplierRepository.existsByName(NAME)).thenReturn(false);
        when(supplierRepository.existsByNit(NIT)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> supplierService.saveSupplier(createSupplierRequestDTO));
        verify(supplierRepository).existsByName(NAME);
        verify(supplierRepository).existsByNit(NIT);
        verify(supplierRepository, never()).save(any());
    }

}
