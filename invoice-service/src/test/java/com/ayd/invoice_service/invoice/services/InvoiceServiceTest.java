package com.ayd.invoice_service.invoice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.ports.ConfigClientPort;
import com.ayd.invoice_service.Invoice.ports.EmployeeClientPort;
import com.ayd.invoice_service.Invoice.ports.ForInvoiceDetailPort;
import com.ayd.invoice_service.Invoice.ports.InventoryClientPort;
import com.ayd.invoice_service.Invoice.repositories.InvoiceRepository;
import com.ayd.invoice_service.Invoice.services.InvoiceService;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.ayd.sharedInventoryService.cashRegister.dto.CashRegisterResponseDTO;
import com.ayd.sharedInventoryService.warehouse.dto.WarehouseResponseDTO;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ForInvoiceDetailPort forInvoiceDetailPort;

    @Mock
    private InventoryClientPort inventoryClientPort;

    @Mock
    private EmployeeClientPort employeeClientPort;

    @Mock
    private ConfigClientPort configClientPort;

    @InjectMocks
    private InvoiceService invoiceService;

    private static final String INVOICE_ID = "inv-001";
    private static final String CLIENT_DOC = "1234567890101";
    private static final BigDecimal SUBTOTAL = BigDecimal.valueOf(100);
    private static final BigDecimal TAX = SUBTOTAL.multiply(BigDecimal.valueOf(0.12));
    private static final BigDecimal TOTAL = SUBTOTAL.add(TAX);
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.CASH;

    // Valores del prametro
    private static final String PARAM_KEY = "KEY";
    private static final String PARAM_VALUE = "{\"value\": 12}";
    private static final String PARAM_NAME = "Parameter";

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

    @Test
    public void getPaymentMethodsShouldReturnAllOptions() {
        // Act
        var result = invoiceService.getPaymentMethods();

        // Assert
        assertEquals(3, result.size());
        assertEquals("Tarjeta", result.get(0).getName());
        assertEquals("Efectivo", result.get(1).getName());
        assertEquals("Online", result.get(2).getName());
    }

    @Test
    public void getItemTypesShouldReturnAllTypes() {
        // Act
        var result = invoiceService.getItemTypes();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Bienes", result.get(0).getName());
        assertEquals("Servicios", result.get(1).getName());
    }

    @Test
    public void getAllInvoicesByIdsShouldReturnMatchingInvoices() {
        // Arrange
        List<String> ids = List.of(INVOICE_ID);
        when(invoiceRepository.findAllByIdIn(ids)).thenReturn(List.of(invoice));

        // Act
        List<Invoice> result = invoiceService.getAllInvoicesByIds(ids);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository).findAllByIdIn(ids);
    }

    @Test
    public void createInvoiceIdentifyEmplooyeWarehouseShouldDelegateUsingEmployeeWarehouse() throws NotFoundException {
        // Arrange
        var auth = new UsernamePasswordAuthenticationToken("juan", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var employee = new EmployeeResponseDTO(
                "emp-1", "1234567890101", "Juan", "Pérez",
                BigDecimal.valueOf(5000), BigDecimal.valueOf(4.83),
                BigDecimal.valueOf(1.00), null, null);

        var warehouse = new WarehouseResponseDTO("wh-1", "Central", "Zona 1", true);
        var cashRegister = new CashRegisterResponseDTO("cr-1", "REG001", "emp-1", true, warehouse);

        when(employeeClientPort.findEmployeeByUserName("juan")).thenReturn(employee);
        when(inventoryClientPort.findByEmployeeId("emp-1")).thenReturn(cashRegister);

        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(forInvoiceDetailPort.calcValuesInvoiceDetail(any())).thenReturn(SUBTOTAL);
        when(forInvoiceDetailPort.createInvoiceDetail(any(), any())).thenReturn(List.of());

        String PARAM_KEY = "REGIMEN";
        String PARAM_VALUE = "{\"value\": 12}";
        String PARAM_NAME = "IVA general";

        ParameterResponseDTO parameter = new ParameterResponseDTO(PARAM_KEY, PARAM_VALUE, PARAM_NAME);
        when(configClientPort.getRegimenParameter()).thenReturn(parameter);

        // Act
        Invoice result = invoiceService.createInvoiceIdentifyEmplooyeWarehouse(createInvoiceRequestDTO);

        // Assert
        assertNotNull(result);
        verify(employeeClientPort).findEmployeeByUserName("juan");
        verify(inventoryClientPort).findByEmployeeId("emp-1");
        verify(configClientPort).getRegimenParameter();
        verify(invoiceRepository).save(any());
        verify(invoiceRepository).findById(any());
    }

    @Test
    public void createInvoiceShouldSucceedWhenValid() throws NotFoundException {
        // Arrange
        when(forInvoiceDetailPort.calcValuesInvoiceDetail(any())).thenReturn(SUBTOTAL);

        ParameterResponseDTO parameter = new ParameterResponseDTO(PARAM_KEY,
                PARAM_VALUE, PARAM_NAME);
        when(configClientPort.getRegimenParameter()).thenReturn(parameter);

        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(forInvoiceDetailPort.createInvoiceDetail(any(),
                any())).thenReturn(List.of(new InvoiceDetail()));
        when(invoiceRepository.findById(INVOICE_ID)).thenReturn(Optional.of(invoice));

        // Act
        Invoice result = invoiceService.createInvoice(createInvoiceRequestDTO,
                "wh-001");

        // Assert
        assertNotNull(result);
        assertEquals(INVOICE_ID, result.getId());
        verify(invoiceRepository).save(any());
        verify(forInvoiceDetailPort).createInvoiceDetail(any(), any());
        verify(invoiceRepository).findById(INVOICE_ID);
        verify(configClientPort).getRegimenParameter();
    }

    @Test
    public void createInvoiceByWarehouseIdShouldDelegateToCreateInvoice() throws NotFoundException {
        // Arrange
        Invoice mockInvoice = new Invoice();
        when(invoiceRepository.save(any())).thenReturn(mockInvoice);
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(mockInvoice));
        when(forInvoiceDetailPort.calcValuesInvoiceDetail(any())).thenReturn(SUBTOTAL);
        when(forInvoiceDetailPort.createInvoiceDetail(any(),
                any())).thenReturn(List.of());

        ParameterResponseDTO parameter = new ParameterResponseDTO(CLIENT_DOC,
                PARAM_VALUE, PARAM_NAME);
        when(configClientPort.getRegimenParameter()).thenReturn(parameter);

        // Act
        Invoice result = invoiceService.createInvoiceByWarehouseId(createInvoiceRequestDTO, "wh-001");

        // Assert
        assertNotNull(result);
        verify(invoiceRepository).save(any());
    }

}
