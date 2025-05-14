package com.ayd.inventory_service.warehouse.specifications;

import com.ayd.inventory_service.warehouse.models.Warehouse;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WarehouseSpecificationTest {

    private static final String NAME = "Bodega Central";
    private static final String UBICATION = "Zona 4, Ciudad";
    private static final Boolean ACTIVE = true;

    @Mock private Root<Warehouse> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForName() {
        Path<String> path = mock(Path.class);
        when(root.get("name")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + NAME.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = WarehouseSpecification.hasName(NAME).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullName() {
        assertNull(WarehouseSpecification.hasName(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForUbication() {
        Path<String> path = mock(Path.class);
        when(root.get("ubication")).thenReturn((Path)path);
        when(cb.lower(path)).thenReturn(path);
        when(cb.like(path, "%" + UBICATION.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = WarehouseSpecification.hasUbication(UBICATION).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullUbication() {
        assertNull(WarehouseSpecification.hasUbication(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForActive() {
        Path<Boolean> path = mock(Path.class);
        when(root.get("active")).thenReturn((Path)path);
        when(cb.equal(path, ACTIVE)).thenReturn(predicate);

        Predicate result = WarehouseSpecification.isActive(ACTIVE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullActive() {
        assertNull(WarehouseSpecification.isActive(null).toPredicate(root, query, cb));
    }
}
