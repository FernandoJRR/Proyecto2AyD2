package com.ayd.product_service.packages.services;

import org.springframework.stereotype.Service;

import com.ayd.product_service.packages.ports.ForPackagePort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PackageService implements ForPackagePort {

}
