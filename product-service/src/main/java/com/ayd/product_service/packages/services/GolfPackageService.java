package com.ayd.product_service.packages.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.product_service.packages.models.GolfPackage;
import com.ayd.product_service.packages.ports.ForGolfPackageDetailPort;
import com.ayd.product_service.packages.ports.ForGolfPackagePort;
import com.ayd.product_service.packages.repositories.GolfPackageRepository;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class GolfPackageService implements ForGolfPackagePort {

    private final GolfPackageRepository golfPackageRepository;
    private final ForGolfPackageDetailPort forGolfPackageDetailPort;

    @Override
    public GolfPackage createGolfPackage(GolfPackage golfPackage) throws NotFoundException {
        // primero debemos iteramos sobre cada uno de los productos para veficar que
        // existan
        forGolfPackageDetailPort.verifyProductsInDetailExist(golfPackage.getPackageDetail());
        golfPackage.setActive(true);
        golfPackage.getPackageDetail().forEach(detail -> detail.setPackagee(golfPackage));
        // guardamos el paquete si todo esta correcto
        return golfPackageRepository.save(golfPackage);
    }

    @Override
    public GolfPackage updateGolfPackage(String id, GolfPackage golfPackage) throws NotFoundException {

        GolfPackage existingGolfPackage = getGolfPackageById(id);
        // primero debemos iteramos sobre cada uno de los productos para veficar que
        // existan
        forGolfPackageDetailPort.verifyProductsInDetailExist(golfPackage.getPackageDetail());

        // le ponemos el id del existente para que se actialice
        golfPackage.setId(existingGolfPackage.getId());
        golfPackage.getPackageDetail().forEach(detail -> detail.setPackagee(golfPackage));

        // guardamos el paquete si todo esta correcto
        return golfPackageRepository.save(golfPackage);
    }

    @Override
    public GolfPackage getGolfPackageById(String id) throws NotFoundException {
        return golfPackageRepository.findById(id).orElseThrow(() -> new NotFoundException("Paquete no encontrado"));
    }

    @Override
    public List<GolfPackage> getAllGolfPackages() {
        return golfPackageRepository.findAll();
    }

}
