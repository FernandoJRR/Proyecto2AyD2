package com.ayd.reservation_service.reservation.ports;

import com.ayd.sharedInvoiceService.dtos.CreateInvoiceRequestDTO;
import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

public interface ForInvoiceClientPort {

    public InvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceDetailRequestDTO);

    public InvoiceResponseDTO getInvoice(String invoiceId);
}
