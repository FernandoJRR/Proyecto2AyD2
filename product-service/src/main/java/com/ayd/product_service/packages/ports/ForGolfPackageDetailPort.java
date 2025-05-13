package com.ayd.product_service.packages.ports;

import java.util.List;

import com.ayd.product_service.packages.models.GolfPackageDetail;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForGolfPackageDetailPort {

    public boolean verifyProductsInDetailExist(List<GolfPackageDetail> details) throws NotFoundException;
}
