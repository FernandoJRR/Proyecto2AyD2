package com.ayd.invoice_service.Invoice.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.invoice_service.Invoice.dtos.InvoiceDetailResponseDTO;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {
    public InvoiceDetailResponseDTO fromInvoiceDetailToInvoiceDetailResponseDTO(InvoiceDetail invoiceDetail);
    public List<InvoiceDetailResponseDTO> fromInvoiceDetailsToInvoiceDetailResponseDTOs(List<InvoiceDetail> invoiceDetails);
}
