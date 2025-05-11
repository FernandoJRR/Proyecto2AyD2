package com.ayd.invoice_service.Invoice.ports;

import java.util.List;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForInvoicePort {
    public Invoice createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO) throws IllegalArgumentException,NotFoundException;

    public Invoice getInvoiceById(String id) throws NotFoundException;

    public List<Invoice> getInvoicesByClientDocument(String clientDocument);

    public List<Invoice> getAllInvoices(SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO);
}
