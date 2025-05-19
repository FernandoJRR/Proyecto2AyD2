package com.ayd.invoice_service.invoice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.invoice_service.Invoice.adapter.ProductClientAdapter;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.repositories.InvoiceDetailRepository;
import com.ayd.invoice_service.Invoice.services.InvoiceDetailService;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.sharedInvoiceService.enums.ItemType;
import com.ayd.sharedProductService.packages.dtos.GolfPackageDetailResponseDTO;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;
import com.ayd.sharedProductService.product.dtos.ProductResponseDTO;

@ExtendWith(MockitoExtension.class)
public class InvoiceDetailServiceTest {

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @Mock
    private ProductClientAdapter productClientAdapter;

    @InjectMocks
    private InvoiceDetailService invoiceDetailService;

    private static final String DETAIL_ID = "detail-123";
    private static final String ITEM_ID = "item-001";
    private static final String ITEM_NAME = "Medicamento A";
    private static final ItemType ITEM_TYPE = ItemType.GOOD;
    private static final Integer QUANTITY = 3;
    private static final BigDecimal UNIT_PRICE = BigDecimal.valueOf(10.0);
    private static final BigDecimal TOTAL = UNIT_PRICE.multiply(BigDecimal.valueOf(QUANTITY));
    private static final String INVOICE_ID = "invoice-001";

    private CreateInvoiceDetailRequestDTO validRequestDTO;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        validRequestDTO = new CreateInvoiceDetailRequestDTO(ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, UNIT_PRICE);
        invoice = new Invoice();
        invoice.setId(INVOICE_ID);
    }

    /**
     * dado: que se proporciona un ID válido.
     * cuando: se llama al método getInvoiceDetailById.
     * entonces: se retorna el detalle de factura correspondiente.
     */
    @Test
    public void getInvoiceDetailByIdShouldReturnDetailWhenExists() throws NotFoundException {
        // Arrange
        InvoiceDetail detail = new InvoiceDetail(ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, UNIT_PRICE, TOTAL, invoice);
        when(invoiceDetailRepository.findById(DETAIL_ID)).thenReturn(Optional.of(detail));

        // Act
        InvoiceDetail result = invoiceDetailService.getInvoiceDetailById(DETAIL_ID);

        // Assert
        assertNotNull(result);
        assertEquals(ITEM_ID, result.getItemId());
        verify(invoiceDetailRepository).findById(DETAIL_ID);
    }

    /**
     * dado: que el ID no corresponde a ningún detalle.
     * cuando: se llama al método getInvoiceDetailById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void getInvoiceDetailByIdShouldThrowWhenNotFound() {
        // Arrange
        when(invoiceDetailRepository.findById(DETAIL_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> invoiceDetailService.getInvoiceDetailById(DETAIL_ID));
        verify(invoiceDetailRepository).findById(DETAIL_ID);
    }

    /**
     * dado: que la cantidad es cero o menor.
     * cuando: se llama al método createInvoiceDetail.
     * entonces: se lanza IllegalArgumentException.
     */
    @Test
    public void createInvoiceDetailShouldThrowWhenQuantityIsInvalid() {
        // Arrange
        CreateInvoiceDetailRequestDTO invalidDTO = new CreateInvoiceDetailRequestDTO(
                ITEM_ID, ITEM_NAME, ITEM_TYPE, 0, UNIT_PRICE);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> invoiceDetailService.createInvoiceDetail(invalidDTO, invoice));
        verify(invoiceDetailRepository, never()).save(any());
    }

    /**
     * dado: que el precio unitario es cero o negativo.
     * cuando: se llama al método createInvoiceDetail.
     * entonces: se lanza IllegalArgumentException.
     */
    @Test
    public void createInvoiceDetailShouldThrowWhenPriceIsInvalid() {
        // Arrange
        CreateInvoiceDetailRequestDTO invalidDTO = new CreateInvoiceDetailRequestDTO(
                ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, BigDecimal.ZERO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> invoiceDetailService.createInvoiceDetail(invalidDTO, invoice));
        verify(invoiceDetailRepository, never()).save(any());
    }

    /**
     * dado: que se proporciona un ID de factura válido.
     * cuando: se llama a getInvoiceDetailsByInvoiceId.
     * entonces: se retorna la lista de detalles asociados.
     */
    @Test
    public void getInvoiceDetailsByInvoiceIdShouldReturnDetails() {
        // Arrange
        InvoiceDetail detail = new InvoiceDetail(ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, UNIT_PRICE, TOTAL, invoice);
        when(invoiceDetailRepository.findByInvoiceId(INVOICE_ID)).thenReturn(List.of(detail));

        // Act
        List<InvoiceDetail> result = invoiceDetailService.getInvoiceDetailsByInvoiceId(INVOICE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ITEM_ID, result.get(0).getItemId());
        verify(invoiceDetailRepository).findByInvoiceId(INVOICE_ID);
    }

    /**
     * dado: que se proporciona una lista válida de detalles.
     * cuando: se llama a calcValuesInvoiceDetail.
     * entonces: se retorna el total correctamente calculado.
     */
    @Test
    public void calcValuesInvoiceDetailShouldReturnTotalWhenValid() {
        // Arrange
        var inputList = List.of(validRequestDTO);

        // Act
        BigDecimal result = invoiceDetailService.calcValuesInvoiceDetail(inputList);

        // Assert
        assertNotNull(result);
        assertEquals(TOTAL, result);
    }

    /**
     * dado: que un detalle tiene cantidad no válida.
     * cuando: se llama a calcValuesInvoiceDetail.
     * entonces: se lanza IllegalArgumentException.
     */
    @Test
    public void calcValuesInvoiceDetailShouldThrowWhenQuantityInvalid() {
        // Arrange
        var invalidDTO = new CreateInvoiceDetailRequestDTO(ITEM_ID, ITEM_NAME, ITEM_TYPE, 0, UNIT_PRICE);
        var list = List.of(invalidDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> invoiceDetailService.calcValuesInvoiceDetail(list));
    }

    /**
     * dado: que un detalle tiene precio no válido.
     * cuando: se llama a calcValuesInvoiceDetail.
     * entonces: se lanza IllegalArgumentException.
     */
    @Test
    public void calcValuesInvoiceDetailShouldThrowWhenPriceInvalid() {
        // Arrange
        var invalidDTO = new CreateInvoiceDetailRequestDTO(ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, BigDecimal.ZERO);
        var list = List.of(invalidDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> invoiceDetailService.calcValuesInvoiceDetail(list));
    }

    /**
     * dado: que se envía un producto válido tipo GOOD.
     * cuando: se llama a createInvoiceDetail.
     * entonces: se crea y retorna correctamente el detalle.
     */
    @Test
    public void createInvoiceDetailShouldCreateWhenItemTypeIsGood() throws NotFoundException {
        // Arrange
        ProductResponseDTO dto = new ProductResponseDTO(
                ITEM_ID,
                ITEM_NAME,
                "CODE-001",
                "BAR-123",
                UNIT_PRICE,
                "GOOD",
                "ACTIVE",
                "2024-01-01T00:00:00Z");
        InvoiceDetail savedDetail = new InvoiceDetail(ITEM_ID, ITEM_NAME, ITEM_TYPE, QUANTITY, UNIT_PRICE, TOTAL,
                invoice);

        when(productClientAdapter.getProductById(ITEM_ID)).thenReturn(dto);
        when(invoiceDetailRepository.save(any())).thenReturn(savedDetail);

        // Act
        List<InvoiceDetail> result = invoiceDetailService.createInvoiceDetail(validRequestDTO, invoice);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ITEM_ID, result.get(0).getItemId());
        verify(productClientAdapter).getProductById(ITEM_ID);
        verify(invoiceDetailRepository).save(any());
    }

    /**
     * dado: que se envía un paquete válido tipo SERVICE con múltiples productos.
     * cuando: se llama a createInvoiceDetail.
     * entonces: se crean varios detalles de factura correspondientes a los
     * productos del paquete.
     */
    @Test
    public void createInvoiceDetailShouldCreateMultipleWhenItemTypeIsService() throws NotFoundException {
        // Arrange
        CreateInvoiceDetailRequestDTO serviceRequest = new CreateInvoiceDetailRequestDTO(
                "pack-123", "Paquete Salud", ItemType.SERVICE, 2, BigDecimal.valueOf(40));

        ProductResponseDTO product1 = new ProductResponseDTO(
                "p1", "Producto A", "CODE1", "BAR1", BigDecimal.valueOf(20), "GOOD", "ACTIVE", "2024-01-01T00:00:00Z");

        ProductResponseDTO product2 = new ProductResponseDTO(
                "p2", "Producto B", "CODE2", "BAR2", BigDecimal.valueOf(20), "GOOD", "ACTIVE", "2024-01-01T00:00:00Z");

        GolfPackageDetailResponseDTO pd1 = new GolfPackageDetailResponseDTO("pd1", product1, 1);
        GolfPackageDetailResponseDTO pd2 = new GolfPackageDetailResponseDTO("pd2", product2, 2);

        GolfPackageResponseDTO pkg = new GolfPackageResponseDTO(
                "pack-123", "Paquete Salud", "Descripción", BigDecimal.valueOf(40), true, List.of(pd1, pd2));

        when(productClientAdapter.getPackageById("pack-123")).thenReturn(pkg);
        when(invoiceDetailRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<InvoiceDetail> result = invoiceDetailService.createInvoiceDetail(serviceRequest, invoice);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("p1", result.get(0).getItemId());
        assertEquals("p2", result.get(1).getItemId());
        verify(productClientAdapter).getPackageById("pack-123");
        verify(invoiceDetailRepository, times(2)).save(any());
    }

}
