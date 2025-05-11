package com.ayd.invoice_service.Invoice.dtos;

import com.ayd.invoice_service.Invoice.enums.PaymentMethod;

import lombok.Value;

@Value
public class SpecificationInvoiceRequestDTO {
    PaymentMethod paymentMethod;
    String clientDocument;
}