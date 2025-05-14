package com.ayd.invoice_service.Invoice.dtos;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

import com.ayd.sharedInvoiceService.enums.PaymentMethod;

@Value
public class InvoiceResponseDTO {
    String id;
    PaymentMethod paymentMethod;
    BigDecimal subtotal;
    BigDecimal tax;
    BigDecimal total;
    String clientDocument;
    List<InvoiceDetailResponseDTO> details;
}
