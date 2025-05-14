package com.ayd.invoice_service.invoice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.invoice_service.Invoice.enums.ItemType;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.repositories.InvoiceDetailRepository;
import com.ayd.invoice_service.Invoice.services.InvoiceDetailService;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InvoiceDetailServiceTest {

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

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
}
