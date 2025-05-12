package com.ayd.invoice_service.Invoice.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.invoice_service.Invoice.dtos.InvoiceResponseDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    public InvoiceResponseDTO fromInvoiceToInvoiceResponseDTO(Invoice invoice);

    public List<InvoiceResponseDTO> fromInvoicesToInvoiceResponseDTOs(List<Invoice> invoices);
}
