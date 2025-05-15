package com.ayd.invoice_service.Invoice.ports;

import java.util.List;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.ItemTypeResponseDTO;
import com.ayd.invoice_service.Invoice.dtos.PaymentMethodResponse;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForInvoicePort {

    public Invoice createInvoiceByWarehouseId(CreateInvoiceRequestDTO createInvoiceRequestDTO, String warehouseId)
            throws IllegalArgumentException, NotFoundException;

    public Invoice createInvoiceIdentifyEmplooyeWarehouse(CreateInvoiceRequestDTO createInvoiceRequestDTO)
            throws IllegalArgumentException,
            NotFoundException;

    public Invoice createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO, String warehouseId)
            throws IllegalArgumentException, NotFoundException;

    public Invoice getInvoiceById(String id) throws NotFoundException;

    public List<Invoice> getInvoicesByClientDocument(String clientDocument);

    public List<Invoice> getAllInvoices(SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO);

    public List<PaymentMethodResponse> getPaymentMethods();

    public List<ItemTypeResponseDTO> getItemTypes();
}
