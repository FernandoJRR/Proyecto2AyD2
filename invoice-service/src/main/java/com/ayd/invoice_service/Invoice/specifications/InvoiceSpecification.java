package com.ayd.invoice_service.Invoice.specifications;

import com.ayd.invoice_service.Invoice.enums.PaymentMethod;
import com.ayd.invoice_service.Invoice.models.Invoice;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceSpecification {

    public static Specification<Invoice> hasPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, cb) -> paymentMethod == null ? null : cb.equal(root.get("paymentMethod"), paymentMethod);
    }

    public static Specification<Invoice> hasClientDocument(String clientDocument) {
        return (root, query, cb) -> clientDocument == null ? null
                : cb.equal(root.get("clientDocument"), clientDocument);
    }
}