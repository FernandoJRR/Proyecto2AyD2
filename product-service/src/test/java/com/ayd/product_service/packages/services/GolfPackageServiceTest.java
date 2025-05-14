package com.ayd.product_service.packages.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.product_service.packages.models.GolfPackage;
import com.ayd.product_service.packages.models.GolfPackageDetail;
import com.ayd.product_service.packages.ports.ForGolfPackageDetailPort;
import com.ayd.product_service.packages.repositories.GolfPackageRepository;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class GolfPackageServiceTest {

    private static final String PACKAGE_ID = "PKG001";
    private static final String PACKAGE_NAME = "Test Package";
    private static final String PACKAGE_DESCRIPTION = "Test Description";
    private static final BigDecimal PACKAGE_PRICE = BigDecimal.valueOf(199.99);
    private static final Boolean PACKAGE_ACTIVE = true;

    @Mock
    private GolfPackageRepository golfPackageRepository;

    @Mock
    private ForGolfPackageDetailPort forGolfPackageDetailPort;

    @InjectMocks
    private GolfPackageService golfPackageService;

    private GolfPackage golfPackage;
    private GolfPackageDetail detail;

    @BeforeEach
    void setUp() {
        detail = new GolfPackageDetail();
        golfPackage = new GolfPackage(
                PACKAGE_NAME,
                PACKAGE_DESCRIPTION,
                PACKAGE_PRICE,
                List.of(detail),
                PACKAGE_ACTIVE);
    }

    /**
     * dado: paquete válido
     * cuando: se llama createGolfPackage
     * entonces: guarda correctamente
     */
    @Test
    void createGolfPackageSuccess() throws NotFoundException {
        when(forGolfPackageDetailPort.verifyProductsInDetailExist(golfPackage.getPackageDetail())).thenReturn(true);
        when(golfPackageRepository.save(golfPackage)).thenReturn(golfPackage);

        GolfPackage result = golfPackageService.createGolfPackage(golfPackage);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.getActive()),
                () -> assertEquals(golfPackage, result));
    }

    /**
     * dado: paquete válido para update
     * cuando: se llama updateGolfPackage
     * entonces: se actualiza correctamente
     */
    @Test
    void updateGolfPackageSuccess() throws NotFoundException {
        when(golfPackageRepository.findById(PACKAGE_ID)).thenReturn(Optional.of(new GolfPackage()));
        when(forGolfPackageDetailPort.verifyProductsInDetailExist(golfPackage.getPackageDetail())).thenReturn(true);
        when(golfPackageRepository.save(any())).thenReturn(golfPackage);

        GolfPackage result = golfPackageService.updateGolfPackage(PACKAGE_ID, golfPackage);

        assertNotNull(result);
        assertEquals(golfPackage, result);
    }

    /**
     * dado: id existente
     * cuando: se llama getGolfPackageById
     * entonces: retorna el paquete
     */
    @Test
    void getGolfPackageByIdReturnsPackage() throws NotFoundException {
        when(golfPackageRepository.findById(PACKAGE_ID)).thenReturn(Optional.of(golfPackage));

        GolfPackage result = golfPackageService.getGolfPackageById(PACKAGE_ID);

        assertEquals(golfPackage, result);
    }

    /**
     * dado: id inexistente
     * cuando: se llama getGolfPackageById
     * entonces: lanza NotFoundException
     */
    @Test
    void getGolfPackageByIdThrowsNotFoundException() {
        when(golfPackageRepository.findById(PACKAGE_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> golfPackageService.getGolfPackageById(PACKAGE_ID));
    }

    /**
     * dado: repositorio con paquetes
     * cuando: se llama getAllGolfPackages
     * entonces: retorna todos los paquetes
     */
    @Test
    void getAllGolfPackagesReturnsList() {
        when(golfPackageRepository.findAll()).thenReturn(Collections.singletonList(golfPackage));

        List<GolfPackage> result = golfPackageService.getAllGolfPackages();

        assertEquals(1, result.size());
        assertEquals(golfPackage, result.get(0));
    }
}
