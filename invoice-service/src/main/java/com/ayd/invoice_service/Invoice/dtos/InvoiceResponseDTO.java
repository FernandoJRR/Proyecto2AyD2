package com.ayd.invoice_service.Invoice.dtos;

import com.ayd.invoice_service.Invoice.enums.PaymentMethod;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class InvoiceResponseDTO {
    Long id;
    PaymentMethod paymentMethod;
    BigDecimal subtotal;
    BigDecimal tax;
    BigDecimal total;
    String clientDocument;
    List<InvoiceDetailResponseDTO> details;
}