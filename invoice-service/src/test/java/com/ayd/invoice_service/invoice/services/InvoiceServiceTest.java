package com.ayd.invoice_service.invoice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.enums.PaymentMethod;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.ports.ForInvoiceDetailPort;
import com.ayd.invoice_service.Invoice.repositories.InvoiceRepository;
import com.ayd.invoice_service.Invoice.services.InvoiceService;
import com.ayd.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ForInvoiceDetailPort forInvoiceDetailPort;

    @InjectMocks
    private InvoiceService invoiceService;

    private static final String INVOICE_ID = "inv-001";
    private static final String CLIENT_DOC = "1234567890101";
    private static final BigDecimal SUBTOTAL = BigDecimal.valueOf(100);
    private static final BigDecimal TAX = SUBTOTAL.multiply(BigDecimal.valueOf(0.12));
    private static final BigDecimal TOTAL = SUBTOTAL.add(TAX);
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.CASH;

    private Invoice invoice;
    private CreateInvoiceDetailRequestDTO detailDTO;
    private CreateInvoiceRequestDTO createInvoiceRequestDTO;

    @BeforeEach
    void setUp() {
        detailDTO = new CreateInvoiceDetailRequestDTO("item-001", "Paracetamol", null, 2, BigDecimal.valueOf(50));
        createInvoiceRequestDTO = new CreateInvoiceRequestDTO(PAYMENT_METHOD, CLIENT_DOC, List.of(detailDTO));
        invoice = new Invoice();
        invoice.setId(INVOICE_ID);
        invoice.setClientDocument(CLIENT_DOC);
        invoice.setPaymentMethod(PAYMENT_METHOD);
        invoice.setSubtotal(SUBTOTAL);
        invoice.setTax(TAX);
        invoice.setTotal(TOTAL);
    }

    /**
     * dado: que los datos del DTO son válidos.
     * cuando: se llama al método createInvoice.
     * entonces: se crea correctamente y se retorna la factura persistida.
     */
    @Test
    public void createInvoiceShouldSucceedWhenValid()
            throws NotFoundException {
        // Arrange
        when(forInvoiceDetailPort.calcValuesInvoiceDetail(any())).thenReturn(SUBTOTAL);
        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(forInvoiceDetailPort.createInvoiceDetail(any(), any())).thenReturn(new InvoiceDetail());
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));

        // Act
        Invoice result = invoiceService.createInvoice(createInvoiceRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(INVOICE_ID, result.getId());
        assertEquals(CLIENT_DOC, result.getClientDocument());
        assertEquals(TOTAL, result.getTotal());
        verify(invoiceRepository).save(any());
        verify(forInvoiceDetailPort).calcValuesInvoiceDetail(any());
        verify(forInvoiceDetailPort).createInvoiceDetail(any(), any());
        verify(invoiceRepository).findById(INVOICE_ID);
    }

    /**
     * dado: que el DTO no contiene detalles.
     * cuando: se llama a createInvoice.
     * entonces: se lanza IllegalArgumentException.
     */
    @Test
    public void createInvoiceShouldThrowWhenNoDetailsProvided() {
        // Arrange
        CreateInvoiceRequestDTO invalidDTO = new CreateInvoiceRequestDTO(PAYMENT_METHOD, CLIENT_DOC, List.of());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> invoiceService.createInvoice(invalidDTO));
        verify(invoiceRepository, never()).save(any());
    }

    /**
     * dado: que no se encuentra la factura guardada por su ID.
     * cuando: se llama a createInvoice.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void createInvoiceShouldThrowWhenSavedInvoiceNotFound() throws NotFoundException {
        // Arrange
        when(forInvoiceDetailPort.calcValuesInvoiceDetail(any())).thenReturn(SUBTOTAL);
        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(forInvoiceDetailPort.createInvoiceDetail(any(), any())).thenReturn(new InvoiceDetail());
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> invoiceService.createInvoice(createInvoiceRequestDTO));
        verify(invoiceRepository).save(any());
        verify(invoiceRepository).findById(INVOICE_ID);
    }

    /**
     * dado: que la factura existe por ID.
     * cuando: se llama a getInvoiceById.
     * entonces: se retorna correctamente.
     */
    @Test
    public void getInvoiceByIdShouldReturnWhenExists() throws NotFoundException {
        // Arrange
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));

        // Act
        Invoice result = invoiceService.getInvoiceById(INVOICE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(INVOICE_ID, result.getId());
        verify(invoiceRepository).findById(INVOICE_ID);
    }

    /**
     * dado: que no existe una factura con el ID dado.
     * cuando: se llama a getInvoiceById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void getInvoiceByIdShouldThrowWhenNotFound() {
        // Arrange
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> invoiceService.getInvoiceById(INVOICE_ID));
        verify(invoiceRepository).findById(INVOICE_ID);
    }

    /**
     * dado: que se busca por documento del cliente.
     * cuando: se llama a getInvoicesByClientDocument.
     * entonces: se retorna la lista correspondiente.
     */
    @Test
    public void getInvoicesByClientDocumentShouldReturnList() {
        // Arrange
        when(invoiceRepository.findByClientDocument(CLIENT_DOC)).thenReturn(List.of(invoice));

        // Act
        List<Invoice> result = invoiceService.getInvoicesByClientDocument(CLIENT_DOC);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository).findByClientDocument(CLIENT_DOC);
    }

    /**
     * dado: que no se envía specification.
     * cuando: se llama a getAllInvoices.
     * entonces: se retorna todo el contenido.
     */
    @Test
    public void getAllInvoicesShouldReturnAllWhenSpecificationIsNull() {
        // Arrange
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        // Act
        List<Invoice> result = invoiceService.getAllInvoices(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository).findAll();
    }

    /**
     * dado: que se envía un specification con filtros.
     * cuando: se llama a getAllInvoices.
     * entonces: se aplica el filtro y retorna la lista filtrada.
     */
    @Test
    public void getAllInvoicesShouldApplySpecificationWhenProvided() {
        // Arrange
        SpecificationInvoiceRequestDTO dto = new SpecificationInvoiceRequestDTO(PAYMENT_METHOD, CLIENT_DOC);
        when(invoiceRepository.findAll(any(Specification.class))).thenReturn(List.of(invoice));

        // Act
        List<Invoice> result = invoiceService.getAllInvoices(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository).findAll(any(Specification.class));
    }
}