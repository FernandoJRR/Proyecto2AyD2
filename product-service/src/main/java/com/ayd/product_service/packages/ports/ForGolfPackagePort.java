package com.ayd.product_service.packages.ports;

import java.util.List;

import com.ayd.product_service.packages.models.GolfPackage;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForGolfPackagePort {

    public GolfPackage createGolfPackage(GolfPackage golfPackage) throws NotFoundException;

    public GolfPackage updateGolfPackage(String id, GolfPackage golfPackage) throws NotFoundException;

    public GolfPackage getGolfPackageById(String id) throws NotFoundException;

    public List<GolfPackage> getAllGolfPackages();
}
