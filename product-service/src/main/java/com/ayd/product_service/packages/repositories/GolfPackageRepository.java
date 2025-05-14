package com.ayd.product_service.packages.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.product_service.packages.models.GolfPackage;

public interface GolfPackageRepository extends JpaRepository<GolfPackage, String> {

}
