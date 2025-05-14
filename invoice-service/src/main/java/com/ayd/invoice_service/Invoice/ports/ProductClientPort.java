package com.ayd.invoice_service.Invoice.ports;

import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;
import com.ayd.sharedProductService.product.dtos.ProductResponseDTO;

public interface ProductClientPort {
    public GolfPackageResponseDTO getPackageById(String packageId) throws NotFoundException;

    public ProductResponseDTO getProductById(String productId) throws NotFoundException;
}
