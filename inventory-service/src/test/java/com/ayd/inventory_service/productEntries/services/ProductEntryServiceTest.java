package com.ayd.inventory_service.productEntries.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntryRequestDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntrySpecificationDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;
import com.ayd.inventory_service.productEntries.ports.ForProductEntryDetailPort;
import com.ayd.inventory_service.productEntries.repositories.ProductEntryRepository;
import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.ports.ForStockPort;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.ports.ForSupplierPort;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ProductEntryServiceTest {

    @Mock
    private ProductEntryRepository productEntryRepository;

    @Mock
    private ForProductEntryDetailPort forProductEntryDetailPort;

    @Mock
    private ForStockPort forStockPort;

    @Mock
    private ForWarehousePort forWarehousePort;

    @Mock
    private ForSupplierPort forSupplierPort;

    @InjectMocks
    private ProductEntryService productEntryService;

    private static final String ENTRY_ID = "entry-id";
    private static final String INVOICE_NUMBER = "INV-001";
    private static final String WAREHOUSE_ID = "warehouse-id";
    private static final String SUPPLIER_ID = "supplier-id";
    private static final String ID = "product-id";
    private static final int QUANTITY = 10;

    private ProductEntry productEntry;
    private Warehouse warehouse;
    private Supplier supplier;
    private ProductEntryDetailRequestDTO detailDTO;
    private ProductEntryRequestDTO entryRequestDTO;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        supplier = new Supplier();

        detailDTO = new ProductEntryDetailRequestDTO(ID, QUANTITY, new java.math.BigDecimal("15.00"));
        entryRequestDTO = new ProductEntryRequestDTO(
                INVOICE_NUMBER,
                LocalDate.now(),
                WAREHOUSE_ID,
                SUPPLIER_ID,
                List.of(detailDTO));

        productEntry = new ProductEntry(entryRequestDTO, warehouse, supplier);
        productEntry.setId(ENTRY_ID);
    }

    /**
     * dado: un ID válido de entrada de producto.
     * cuando: se llama al método getProductEntryById.
     * entonces: se retorna correctamente la entrada de producto.
     */
    @Test
    void getProductEntryByIdShouldReturnEntryWhenExists() throws NotFoundException {
        when(productEntryRepository.findById(ENTRY_ID)).thenReturn(Optional.of(productEntry));

        ProductEntry result = productEntryService.getProductEntryById(ENTRY_ID);

        assertNotNull(result);
        assertEquals(ENTRY_ID, result.getId());
        verify(productEntryRepository).findById(ENTRY_ID);
    }

    /**
     * dado: un ID inválido.
     * cuando: se llama al método getProductEntryById.
     * entonces: se lanza una excepción NotFoundException.
     */
    @Test
    void getProductEntryByIdShouldThrowWhenNotFound() {
        when(productEntryRepository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productEntryService.getProductEntryById(ENTRY_ID));
        verify(productEntryRepository).findById(ENTRY_ID);
    }

    /**
     * dado: un número de factura existente.
     * cuando: se llama al método getProductEntryByInvoiceNumber.
     * entonces: se retorna correctamente la entrada de producto.
     */
    @Test
    void getProductEntryByInvoiceNumberShouldReturnEntryWhenExists() throws NotFoundException {
        when(productEntryRepository.findByInvoiceNumber(INVOICE_NUMBER)).thenReturn(Optional.of(productEntry));

        ProductEntry result = productEntryService.getProductEntryByInvoiceNumber(INVOICE_NUMBER);

        assertNotNull(result);
        assertEquals(INVOICE_NUMBER, result.getInvoiceNumber());
        verify(productEntryRepository).findByInvoiceNumber(INVOICE_NUMBER);
    }

    /**
     * dado: un número de factura inexistente.
     * cuando: se llama al método getProductEntryByInvoiceNumber.
     * entonces: se lanza una excepción NotFoundException.
     */
    @Test
    void getProductEntryByInvoiceNumberShouldThrowWhenNotFound() {
        when(productEntryRepository.findByInvoiceNumber(INVOICE_NUMBER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productEntryService.getProductEntryByInvoiceNumber(INVOICE_NUMBER));
        verify(productEntryRepository).findByInvoiceNumber(INVOICE_NUMBER);
    }

    /**
     * dado: una solicitud de entrada de producto válida y sin duplicados.
     * cuando: se llama al método saveProductEntry.
     * entonces: se guarda la entrada, se crean los detalles, se actualiza el stock
     * y se retorna la entrada.
     */
    @Test
    void saveProductEntryShouldSaveSuccessfully() throws Exception {
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(forSupplierPort.getSupplierById(SUPPLIER_ID)).thenReturn(supplier);
        when(productEntryRepository.existsByInvoiceNumber(INVOICE_NUMBER)).thenReturn(false);
        when(productEntryRepository.save(any())).thenAnswer(inv -> {
            ProductEntry saved = inv.getArgument(0);
            saved.setId(ENTRY_ID);
            return saved;
        });
        when(productEntryRepository.findById(ENTRY_ID)).thenReturn(Optional.of(productEntry));

        ProductEntry result = productEntryService.saveProductEntry(entryRequestDTO);

        assertNotNull(result);
        assertEquals(INVOICE_NUMBER, result.getInvoiceNumber());

        verify(forWarehousePort).getWarehouse(WAREHOUSE_ID);
        verify(forSupplierPort).getSupplierById(SUPPLIER_ID);
        verify(productEntryRepository).existsByInvoiceNumber(INVOICE_NUMBER);
        verify(productEntryRepository).save(any());
        verify(forProductEntryDetailPort, times(1)).saveProductEntryDetail(any(), eq(productEntry));
        verify(forStockPort, times(1)).addStockByProductIdAndWarehouseId(ID, warehouse, QUANTITY);
        verify(productEntryRepository).findById(ENTRY_ID);
    }

    /**
     * dado: que ya existe una entrada con el mismo número de factura.
     * cuando: se llama al método saveProductEntry.
     * entonces: se lanza una DuplicatedEntryException y no se guarda.
     * 
     * @throws NotFoundException
     */
    @Test
    void saveProductEntryShouldThrowWhenInvoiceNumberExists() throws NotFoundException {
        when(productEntryRepository.existsByInvoiceNumber(INVOICE_NUMBER)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> productEntryService.saveProductEntry(entryRequestDTO));

        verify(productEntryRepository).existsByInvoiceNumber(INVOICE_NUMBER);
        verify(productEntryRepository, never()).save(any());
        verify(forProductEntryDetailPort, never()).saveProductEntryDetail(any(), any());
        verify(forStockPort, never()).addStockByProductIdAndWarehouseId(any(), any(), anyInt());
    }

    /**
     * dado: que existen entradas de producto en la base de datos.
     * cuando: se llama al método getAllProductEntries.
     * entonces: se retorna una lista con las entradas existentes.
     */
    @Test
    void getAllProductEntriesShouldReturnList() {
        List<ProductEntry> mockEntries = List.of(productEntry);
        when(productEntryRepository.findAll()).thenReturn(mockEntries);

        List<ProductEntry> result = productEntryService.getAllProductEntries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(INVOICE_NUMBER, result.get(0).getInvoiceNumber());

        verify(productEntryRepository).findAll();
    }

    /**
     * dado: que el DTO de especificación es null.
     * cuando: se llama a getAlByProductEntrieSpecification.
     * entonces: se retorna la lista completa de entradas de producto.
     */
    @Test
    void getAllBySpecificationShouldReturnAllWhenDtoIsNull() {
        when(productEntryRepository.findAll()).thenReturn(List.of(productEntry));

        List<ProductEntry> result = productEntryService.getAlByProductEntrieSpecification(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productEntryRepository).findAll();
    }

    /**
     * dado: un DTO de especificación con filtros válidos.
     * cuando: se llama a getAlByProductEntrieSpecification.
     * entonces: se usa la specification y se retorna la lista filtrada.
     */
    @Test
    void getAllBySpecificationShouldReturnFilteredListWhenDtoProvided() {
        ProductEntrySpecificationDTO specDTO = new ProductEntrySpecificationDTO(
                INVOICE_NUMBER, ENTRY_ID, WAREHOUSE_ID, SUPPLIER_ID, LocalDate.now());
        when(productEntryRepository.findAll(any(Specification.class))).thenReturn(List.of(productEntry));

        List<ProductEntry> result = productEntryService.getAlByProductEntrieSpecification(specDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productEntryRepository).findAll(any(Specification.class));
    }

}