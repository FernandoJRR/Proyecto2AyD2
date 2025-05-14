package com.ayd.invoice_service.Invoice.ports;

import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;

public interface ProductClientPort {
    public GolfPackageResponseDTO getPackageById(String packageId) throws NotFoundException;
}
