package com.ayd.invoice_service.invoice.specifications;

import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.specifications.InvoiceSpecification;
import com.ayd.sharedInvoiceService.enums.PaymentMethod;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceSpecificationTest {

    private static final String CLIENT_DOCUMENT = "123456789";
    private static final PaymentMethod PAYMENT_METHOD = PaymentMethod.CASH;

    @Mock private Root<Invoice> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * dado: que se proporciona un método de pago.
     * cuando: se llama a hasPaymentMethod.
     * entonces: se construye y retorna el predicate correspondiente.
     */
    @Test
    void shouldReturnPredicateForPaymentMethod() {
        Path<PaymentMethod> path = mock(Path.class);
        when(root.get("paymentMethod")).thenReturn((Path)path);
        when(cb.equal(path, PAYMENT_METHOD)).thenReturn(predicate);

        Predicate result = InvoiceSpecification.hasPaymentMethod(PAYMENT_METHOD).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    /**
     * dado: que se proporciona un documento válido.
     * cuando: se llama a hasClientDocument.
     * entonces: se construye y retorna el predicate correspondiente.
     */
    @Test
    void shouldReturnPredicateForClientDocument() {
        Path<String> path = mock(Path.class);
        when(root.get("clientDocument")).thenReturn((Path)path);
        when(cb.equal(path, CLIENT_DOCUMENT)).thenReturn(predicate);

        Predicate result = InvoiceSpecification.hasClientDocument(CLIENT_DOCUMENT).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    /**
     * dado: que el método de pago es null.
     * cuando: se llama a hasPaymentMethod.
     * entonces: se retorna null.
     */
    @Test
    void shouldReturnNullForNullPaymentMethod() {
        Predicate result = InvoiceSpecification.hasPaymentMethod(null).toPredicate(root, query, cb);
        assertNull(result);
    }

    /**
     * dado: que el documento es null.
     * cuando: se llama a hasClientDocument.
     * entonces: se retorna null.
     */
    @Test
    void shouldReturnNullForNullClientDocument() {
        Predicate result = InvoiceSpecification.hasClientDocument(null).toPredicate(root, query, cb);
        assertNull(result);
    }
}
