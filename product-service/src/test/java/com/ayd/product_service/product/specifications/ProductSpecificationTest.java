package com.ayd.product_service.product.specifications;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.specifitacions.ProductSpecification;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductSpecificationTest {

    private static final String NAME = "Aspirina";
    private static final String CODE = "MED-123";
    private static final String BARCODE = "123456789";
    private static final EnumProductType TYPE = EnumProductType.GOOD;
    private static final EnumProductState STATE = EnumProductState.ACTIVE;

    @Mock private Root<Product> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;
    @Mock private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPredicateForName() {
        Path<String> namePath = mock(Path.class);
        when(root.get("name")).thenReturn((Path) namePath);
        when(cb.lower(namePath)).thenReturn(namePath);
        when(cb.like(namePath, "%" + NAME.toLowerCase() + "%")).thenReturn(predicate);
    
        Predicate result = ProductSpecification.hasName(NAME).toPredicate(root, query, cb);
        assertNotNull(result);
    }
    
    @Test
    void shouldReturnPredicateForCode() {
        Path<String> codePath = mock(Path.class);
        when(root.get("code")).thenReturn((Path) codePath);
        when(cb.lower(codePath)).thenReturn(codePath);
        when(cb.like(codePath, "%" + CODE.toLowerCase() + "%")).thenReturn(predicate);
    
        Predicate result = ProductSpecification.hasCode(CODE).toPredicate(root, query, cb);
        assertNotNull(result);
    }
    
    @Test
    void shouldReturnPredicateForBarCode() {
        Path<String> barCodePath = mock(Path.class);
        when(root.get("barCode")).thenReturn((Path) barCodePath);
        when(cb.lower(barCodePath)).thenReturn(barCodePath);
        when(cb.like(barCodePath, "%" + BARCODE.toLowerCase() + "%")).thenReturn(predicate);
    
        Predicate result = ProductSpecification.hasBarCode(BARCODE).toPredicate(root, query, cb);
        assertNotNull(result);
    }
    
    @Test
    void shouldReturnPredicateForType() {
        Path<EnumProductType> typePath = mock(Path.class);
        when(root.get("type")).thenReturn((Path) typePath);
        when(cb.equal(typePath, TYPE)).thenReturn(predicate);
    
        Predicate result = ProductSpecification.hasType(TYPE).toPredicate(root, query, cb);
        assertNotNull(result);
    }
    
    @Test
    void shouldReturnPredicateForState() {
        Path<EnumProductState> statePath = mock(Path.class);
        when(root.get("state")).thenReturn((Path) statePath);
        when(cb.equal(statePath, STATE)).thenReturn(predicate);
    
        Predicate result = ProductSpecification.hasState(STATE).toPredicate(root, query, cb);
        assertNotNull(result);
    }

    @Test
    void shouldReturnNullForNullName() {
        assertNull(ProductSpecification.hasName(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnNullForNullCode() {
        assertNull(ProductSpecification.hasCode(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnNullForNullBarCode() {
        assertNull(ProductSpecification.hasBarCode(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnNullForNullType() {
        assertNull(ProductSpecification.hasType(null).toPredicate(root, query, cb));
    }

    @Test
    void shouldReturnNullForNullState() {
        assertNull(ProductSpecification.hasState(null).toPredicate(root, query, cb));
    }
}