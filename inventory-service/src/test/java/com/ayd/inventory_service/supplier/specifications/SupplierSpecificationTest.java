package com.ayd.inventory_service.supplier.specifications;

import com.ayd.inventory_service.supplier.models.Supplier;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierSpecificationTest {

    private static final String NIT = "1234567-K";
    private static final String NAME = "Proveedor Ejemplo";
    private static final BigDecimal TAX_REGIME = new BigDecimal("12.00");
    private static final String ADDRESS = "Zona 1, Ciudad";
    private static final Boolean ACTIVE = true;

    @Mock private Root<Supplier> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForNit() {
        Path<String> path = mock(Path.class);
        when(root.get("nit")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + NIT.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = SupplierSpecification.hasNit(NIT).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullNit() {
        assertNull(SupplierSpecification.hasNit(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForName() {
        Path<String> path = mock(Path.class);
        when(root.get("name")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + NAME.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = SupplierSpecification.hasName(NAME).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullName() {
        assertNull(SupplierSpecification.hasName(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForTaxRegime() {
        Path<BigDecimal> path = mock(Path.class);
        when(root.get("taxRegime")).thenReturn((Path)path);
        when(cb.equal(path, TAX_REGIME)).thenReturn(predicate);

        Predicate result = SupplierSpecification.hasTaxRegime(TAX_REGIME).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullTaxRegime() {
        assertNull(SupplierSpecification.hasTaxRegime(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForAddress() {
        Path<String> path = mock(Path.class);
        when(root.get("address")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + ADDRESS.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = SupplierSpecification.hasAddress(ADDRESS).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullAddress() {
        assertNull(SupplierSpecification.hasAddress(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForActive() {
        Path<Boolean> path = mock(Path.class);
        when(root.get("active")).thenReturn((Path)path);
        when(cb.equal(path, ACTIVE)).thenReturn(predicate);

        Predicate result = SupplierSpecification.isActive(ACTIVE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullActive() {
        assertNull(SupplierSpecification.isActive(null).toPredicate(root, query, cb));
    }
}
