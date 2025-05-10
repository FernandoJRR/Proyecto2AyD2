package com.ayd.inventory_service.cashRegister.specifications;

import com.ayd.inventory_service.cashRegister.models.CashRegister;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CashRegisterSpecificationTest {

    private static final String CODE = "CR-001";
    private static final Boolean ACTIVE = true;
    private static final String EMPLOYEE_ID = "emp-123";
    private static final String WAREHOUSE_ID = "wh-456";

    @Mock private Root<CashRegister> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;
    @Mock private Join<Object, Object> join;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForCode() {
        Path<String> codePath = mock(Path.class);
        when(root.get("code")).thenReturn((Path)codePath);
        when(cb.lower(codePath)).thenReturn(codePath);
        when(cb.like(codePath, "%" + CODE.toLowerCase() + "%")).thenReturn(predicate);

        Predicate result = CashRegisterSpecification.hasCode(CODE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullCode() {
        assertNull(CashRegisterSpecification.hasCode(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForActive() {
        Path<Boolean> activePath = mock(Path.class);
        when(root.get("active")).thenReturn((Path)activePath);
        when(cb.equal(activePath, ACTIVE)).thenReturn(predicate);

        Predicate result = CashRegisterSpecification.hasActive(ACTIVE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullActive() {
        assertNull(CashRegisterSpecification.hasActive(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForWarehouseId() {
        Path<String> idPath = mock(Path.class);
        when(root.join("warehouse")).thenReturn(join);
        when(join.get("id")).thenReturn((Path)idPath);
        when(cb.equal(idPath, WAREHOUSE_ID)).thenReturn(predicate);

        Predicate result = CashRegisterSpecification.hasWarehouseId(WAREHOUSE_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullWarehouseId() {
        assertNull(CashRegisterSpecification.hasWarehouseId(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnPredicateForEmployeeId() {
        Path<String> employeeIdPath = mock(Path.class);
        when(root.get("employeeId")).thenReturn((Path)employeeIdPath);
        when(cb.equal(employeeIdPath, EMPLOYEE_ID)).thenReturn(predicate);

        Predicate result = CashRegisterSpecification.hasEmployeeId(EMPLOYEE_ID).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullEmployeeId() {
        assertNull(CashRegisterSpecification.hasEmployeeId(null).toPredicate(root, query, cb));
    }
}
