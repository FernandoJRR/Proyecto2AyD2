package com.ayd.product_service.packages.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.product_service.packages.models.GolfPackageDetail;
import com.ayd.product_service.packages.ports.ForGolfPackageDetailPort;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.ports.ForProductPort;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class GolfPackageDetailService implements ForGolfPackageDetailPort {

    private final ForProductPort forProductPort;

    @Override
    public boolean verifyProductsInDetailExist(List<GolfPackageDetail> details) throws NotFoundException {
        for (GolfPackageDetail detail : details) {
            // si no existe esto lanza excepcion
            Product product = forProductPort.getProduct(detail.getProduct().getId());
            detail.setProduct(product);
        }

        return true;
    }
}
