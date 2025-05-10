package com.ayd.inventory_service.productEntries.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;
import com.ayd.inventory_service.productEntries.repositories.ProductEntryDetailRepository;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductEntryDetailServiceTest {

    @Mock
    private ProductEntryDetailRepository productEntryDetailRepository;

    @InjectMocks
    private ProductEntryDetailService productEntryDetailService;

    private static final String ENTRY_DETAIL_ID = "entry-detail-001";
    private static final String PRODUCT_ENTRY_ID = "entry-001";
    private static final String PRODUCT_ID = "product-001";
    private static final int QUANTITY = 10;
    private static final BigDecimal UNIT_PRICE = new BigDecimal("15.50");

    private ProductEntryDetailRequestDTO requestDTO;
    private ProductEntry productEntry;
    private ProductEntryDetail productEntryDetail;

    @BeforeEach
    void setUp() {
        requestDTO = new ProductEntryDetailRequestDTO(PRODUCT_ID, QUANTITY, UNIT_PRICE);
        productEntry = new ProductEntry();
        productEntry.setId(PRODUCT_ENTRY_ID);
        productEntryDetail = new ProductEntryDetail(requestDTO, productEntry);
        productEntryDetail.setId(ENTRY_DETAIL_ID);
    }

    /**
     * dado: un ID válido que corresponde a un detalle existente
     * cuando: se llama al método getProductEntryDetailById
     * entonces: se retorna correctamente el detalle
     */
    @Test
    public void shouldReturnProductEntryDetailWhenIdExists() throws NotFoundException {
        // Arrange
        when(productEntryDetailRepository.findById(ENTRY_DETAIL_ID)).thenReturn(Optional.of(productEntryDetail));

        // Act
        ProductEntryDetail result = productEntryDetailService.getProductEntryDetailById(ENTRY_DETAIL_ID);

        // Assert
        assertNotNull(result);
        assertEquals(ENTRY_DETAIL_ID, result.getId());
        verify(productEntryDetailRepository).findById(ENTRY_DETAIL_ID);
    }

    /**
     * dado: un ID inválido que no corresponde a ningún detalle
     * cuando: se llama al método getProductEntryDetailById
     * entonces: se lanza una excepción NotFoundException
     */
    @Test
    public void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(productEntryDetailRepository.findById(ENTRY_DETAIL_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productEntryDetailService.getProductEntryDetailById(ENTRY_DETAIL_ID);
        });

        assertEquals("El detalle de la entrada de producto no existe", exception.getMessage());
        verify(productEntryDetailRepository).findById(ENTRY_DETAIL_ID);
    }

    /**
     * dado: un DTO válido y una entrada de producto existente
     * cuando: se llama al método saveProductEntryDetail
     * entonces: se guarda correctamente el detalle y se retorna
     */
    @Test
    public void shouldSaveAndReturnProductEntryDetail() throws NotFoundException {
        // Arrange
        when(productEntryDetailRepository.save(any(ProductEntryDetail.class)))
                .thenAnswer(invocation -> {
                    ProductEntryDetail detail = invocation.getArgument(0);
                    detail.setId(ENTRY_DETAIL_ID);
                    return detail;
                });

        // Act
        ProductEntryDetail result = productEntryDetailService.saveProductEntryDetail(requestDTO, productEntry);

        // Assert
        assertNotNull(result);
        assertEquals(ENTRY_DETAIL_ID, result.getId());
        assertEquals(PRODUCT_ID, result.getProductId());
        assertEquals(QUANTITY, result.getQuantity());
        assertEquals(UNIT_PRICE, result.getUnitPrice());
        assertEquals(productEntry, result.getProductEntry());

        verify(productEntryDetailRepository).save(any(ProductEntryDetail.class));
    }

    /**
     * dado: un ID válido de entrada de producto con detalles asociados
     * cuando: se llama a getAllProductEntryDetailsByProductEntryId
     * entonces: se retorna la lista de detalles correspondiente
     */
    @Test
    public void shouldReturnDetailsWhenProductEntryIdHasDetails() throws NotFoundException {
        // Arrange
        List<ProductEntryDetail> details = List.of(productEntryDetail);
        when(productEntryDetailRepository.findByProductEntryId(PRODUCT_ENTRY_ID)).thenReturn(details);

        // Act
        List<ProductEntryDetail> result = productEntryDetailService
                .getAllProductEntryDetailsByProductEntryId(PRODUCT_ENTRY_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PRODUCT_ID, result.get(0).getProductId());

        verify(productEntryDetailRepository).findByProductEntryId(PRODUCT_ENTRY_ID);
    }

    /**
     * dado: un ID válido de entrada de producto sin detalles
     * cuando: se llama a getAllProductEntryDetailsByProductEntryId
     * entonces: se lanza NotFoundException
     */
    @Test
    public void shouldThrowNotFoundWhenNoDetailsFound() {
        // Arrange
        when(productEntryDetailRepository.findByProductEntryId(PRODUCT_ENTRY_ID)).thenReturn(List.of());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> productEntryDetailService.getAllProductEntryDetailsByProductEntryId(PRODUCT_ENTRY_ID));

        verify(productEntryDetailRepository).findByProductEntryId(PRODUCT_ENTRY_ID);
    }

}
