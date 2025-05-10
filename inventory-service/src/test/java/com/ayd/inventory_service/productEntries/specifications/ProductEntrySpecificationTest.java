package com.ayd.inventory_service.productEntries.specifications;

import com.ayd.inventory_service.productEntries.models.ProductEntry;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductEntrySpecificationTest {

    private static final String INVOICE_NUMBER = "INV-123";
    private static final String ENTRY_ID = "entry-001";
    private static final String WAREHOUSE_ID = "wh-456";
    private static final String SUPPLIER_ID = "sup-789";
    private static final LocalDate DATE = LocalDate.now();

    @Mock private Root<ProductEntry> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;
    @Mock private Join<Object, Object> join;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForInvoiceNumber() {
        Path<String> path = mock(Path.class);
        when(root.get("invoiceNumber")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + INVOICE_NUMBER.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = ProductEntrySpecificacion.hasInvoiceNumber(INVOICE_NUMBER).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullInvoiceNumber() {
        assertNull(ProductEntrySpecificacion.hasInvoiceNumber(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForId() {
        Path<String> path = mock(Path.class);
        when(root.get("id")).thenReturn((Path)path);
        when(cb.equal(path, ENTRY_ID)).thenReturn(predicate);

        Predicate result = ProductEntrySpecificacion.hasId(ENTRY_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullId() {
        assertNull(ProductEntrySpecificacion.hasId(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForWarehouseId() {
        Path<String> path = mock(Path.class);
        when(root.join("warehouse")).thenReturn(join);
        when(join.get("id")).thenReturn((Path)path);
        when(cb.equal(path, WAREHOUSE_ID)).thenReturn(predicate);

        Predicate result = ProductEntrySpecificacion.hasWarehouseId(WAREHOUSE_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullWarehouseId() {
        assertNull(ProductEntrySpecificacion.hasWarehouseId(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForSupplierId() {
        Path<String> path = mock(Path.class);
        when(root.join("supplier")).thenReturn(join);
        when(join.get("id")).thenReturn((Path)path);
        when(cb.equal(path, SUPPLIER_ID)).thenReturn(predicate);

        Predicate result = ProductEntrySpecificacion.hasSupplierId(SUPPLIER_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullSupplierId() {
        assertNull(ProductEntrySpecificacion.hasSupplierId(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForDate() {
        Path<LocalDate> path = mock(Path.class);
        when(root.get("date")).thenReturn((Path)path);
        when(cb.equal(path, DATE)).thenReturn(predicate);

        Predicate result = ProductEntrySpecificacion.hasDate(DATE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullDate() {
        assertNull(ProductEntrySpecificacion.hasDate(null).toPredicate(root, query, cb));
    }
}
