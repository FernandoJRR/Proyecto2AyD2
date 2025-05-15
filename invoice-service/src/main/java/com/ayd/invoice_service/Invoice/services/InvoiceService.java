package com.ayd.invoice_service.Invoice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ayd.config.AuthenticationFilter;
import com.ayd.invoice_service.Invoice.ports.*;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.ayd.sharedInventoryService.cashRegister.dto.CashRegisterResponseDTO;
import com.ayd.sharedInventoryService.stock.dto.ModifyStockRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ayd.invoice_service.Invoice.dtos.ItemTypeResponseDTO;
import com.ayd.invoice_service.Invoice.dtos.PaymentMethodResponse;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.repositories.InvoiceRepository;
import com.ayd.invoice_service.Invoice.specifications.InvoiceSpecification;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;
import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.enums.ItemType;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class InvoiceService implements ForInvoicePort {

    private final InvoiceRepository invoiceRepository;
    private final ForInvoiceDetailPort forInvoiceDetailPort;
    private final ConfigClientPort configClientPort;
    private final InventoryClientPort inventoryClientPort;
    private final EmployeeClientPort employeeClientPort;

    @Override
    public Invoice createInvoiceByWarehouseId(CreateInvoiceRequestDTO createInvoiceRequestDTO, String warehouseId)
            throws IllegalArgumentException, NotFoundException {
        return createInvoice(createInvoiceRequestDTO, warehouseId);
    }

    @Override
    public Invoice createInvoiceIdentifyEmplooyeWarehouse(CreateInvoiceRequestDTO createInvoiceRequestDTO)
            throws IllegalArgumentException, NotFoundException {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        EmployeeResponseDTO employeeResponseDTO = employeeClientPort.findEmployeeByUserName(username);
        CashRegisterResponseDTO cashRegisterResponseDTO = inventoryClientPort.findByEmployeeId(employeeResponseDTO.getId());
        return createInvoice(createInvoiceRequestDTO, cashRegisterResponseDTO.getWarehouse().getId());
    }

    @Override
    public Invoice createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO, String warehouseId)
            throws IllegalArgumentException, NotFoundException {
        // Verificamos quue el modelo de creacion tenga detalles
        if (createInvoiceRequestDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("La factura debe tener al menos un detalle");
        }
        Invoice invoice = new Invoice();

        invoice.setPaymentMethod(createInvoiceRequestDTO.getPaymentMethod());
        invoice.setClientDocument(createInvoiceRequestDTO.getClientDocument());

        BigDecimal total = forInvoiceDetailPort.calcValuesInvoiceDetail(createInvoiceRequestDTO.getDetails());

        ParameterResponseDTO regimenParameter = configClientPort.getRegimenParameter();
        String jsonValue = regimenParameter.getValue();

        BigDecimal myCompaytax = BigDecimal.valueOf(0.12);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonValue);
            BigDecimal value = root.get("value").decimalValue();
            myCompaytax = value.divide(BigDecimal.valueOf(100));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al obtener el regimen: " + e.getMessage());
        }

        invoice.setSubtotal(total);
        BigDecimal tax = invoice.getSubtotal().multiply(myCompaytax);
        invoice.setTax(tax);
        invoice.setTotal(total.add(tax));

        Invoice saveInvoice = invoiceRepository.save(invoice);

        List<InvoiceDetail> invoiceDetails = new java.util.ArrayList<>();
        for (var detail : createInvoiceRequestDTO.getDetails()) {
            invoiceDetails.addAll(forInvoiceDetailPort.createInvoiceDetail(detail, invoice));
        }
        // Sobre los invoice details se hace la actualizacion del stock
        List<ModifyStockRequest> modifyStockRequests = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : invoiceDetails) {
            if (invoiceDetail.getItemType() == ItemType.GOOD) {
                ModifyStockRequest modifyStockRequest = new ModifyStockRequest(invoiceDetail.getItemId(),
                        invoiceDetail.getQuantity(), warehouseId);
                modifyStockRequests.add(modifyStockRequest);
            }
        }
        inventoryClientPort.substractVariousStockByProductIdAndWarehouseId(modifyStockRequests);
        invoice.setDetails(invoiceDetails);
        return invoiceRepository.findById(saveInvoice.getId())
                .orElseThrow(() -> new NotFoundException("No se encontró la factura con id: " + saveInvoice.getId()));
    }

    @Override
    public Invoice getInvoiceById(String id) throws NotFoundException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la factura con id: " + id));
    }

    @Override
    public List<Invoice> getInvoicesByClientDocument(String clientDocument) {
        return invoiceRepository.findByClientDocument(clientDocument);
    }

    @Override
    public List<Invoice> getAllInvoices(SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO) {
        if (specificationInvoiceRequestDTO == null) {
            return invoiceRepository.findAll();
        }
        Specification<Invoice> spec = Specification
                .where(InvoiceSpecification.hasClientDocument(specificationInvoiceRequestDTO.getClientDocument()))
                .and(InvoiceSpecification.hasPaymentMethod(specificationInvoiceRequestDTO.getPaymentMethod()));
        return invoiceRepository.findAll(spec);
    }

    @Override
    public List<PaymentMethodResponse> getPaymentMethods() {
        List<PaymentMethodResponse> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethodResponse(PaymentMethod.CARD, "Tarjeta"));
        paymentMethods.add(new PaymentMethodResponse(PaymentMethod.CASH, "Efectivo"));
        paymentMethods.add(new PaymentMethodResponse(PaymentMethod.ONLINE, "Online"));
        return paymentMethods;
    }

    @Override
    public List<ItemTypeResponseDTO> getItemTypes() {
        List<ItemTypeResponseDTO> itemTypes = new ArrayList<>();
        itemTypes.add(new ItemTypeResponseDTO(ItemType.GOOD, "Bienes"));
        itemTypes.add(new ItemTypeResponseDTO(ItemType.SERVICE, "Servicios"));
        return itemTypes;
    }

    @Override
    public List<Invoice> getAllInvoicesByIds(List<String> ids) {
        return invoiceRepository.findAllByIdIn(ids);
    }

}
