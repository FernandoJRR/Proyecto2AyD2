package com.ayd.product_service.packages.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.product_service.packages.models.GolfPackageDetail;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.ports.ForProductPort;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class GolfPackageDetailServiceTest {

    private static final String PRODUCT_ID = "P001";

    @Mock
    private ForProductPort forProductPort;

    @InjectMocks
    private GolfPackageDetailService golfPackageDetailService;

    private Product product;
    private GolfPackageDetail detail;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(PRODUCT_ID);

        detail = new GolfPackageDetail();

        Product partialProduct = new Product();
        partialProduct.setId(PRODUCT_ID);
        detail.setProduct(partialProduct);

    }

    /**
     * dado: detalles con productos existentes
     * cuando: se llama verifyProductsInDetailExist
     * entonces: retorna true y actualiza los productos
     */
    @Test
    void verifyProductsInDetailExistReturnsTrue() throws NotFoundException {
        List<GolfPackageDetail> details = List.of(detail);

        when(forProductPort.getProduct(PRODUCT_ID)).thenReturn(product);

        boolean result = golfPackageDetailService.verifyProductsInDetailExist(details);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(product, details.get(0).getProduct()));
    }

    /**
     * dado: detalles con producto inexistente
     * cuando: se llama verifyProductsInDetailExist
     * entonces: lanza NotFoundException
     */
    @Test
    void verifyProductsInDetailExistThrowsNotFoundException() throws NotFoundException {
        List<GolfPackageDetail> details = List.of(detail);

        when(forProductPort.getProduct(PRODUCT_ID)).thenThrow(new NotFoundException(anyString()));

        assertThrows(NotFoundException.class,
                () -> golfPackageDetailService.verifyProductsInDetailExist(details));
    }
}
