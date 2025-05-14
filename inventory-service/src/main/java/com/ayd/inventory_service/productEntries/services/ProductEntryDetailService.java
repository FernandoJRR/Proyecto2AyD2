package com.ayd.inventory_service.productEntries.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;
import com.ayd.inventory_service.productEntries.ports.ForProductEntryDetailPort;
import com.ayd.inventory_service.productEntries.repositories.ProductEntryDetailRepository;
import com.ayd.shared.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProductEntryDetailService implements ForProductEntryDetailPort {

    private final ProductEntryDetailRepository productEntryDetailRepository;

    @Override
    public ProductEntryDetail getProductEntryDetailById(String id) throws NotFoundException {
        return productEntryDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El detalle de la entrada de producto no existe"));
    }

    @Override
    public ProductEntryDetail saveProductEntryDetail(ProductEntryDetailRequestDTO productEntryDetailRequestDTO,
            ProductEntry productEntry) throws NotFoundException {
        ProductEntryDetail productEntryDetail = new ProductEntryDetail(productEntryDetailRequestDTO, productEntry);
        productEntryDetail = productEntryDetailRepository.save(productEntryDetail);
        return productEntryDetail;
    }

    @Override
    public List<ProductEntryDetail> getAllProductEntryDetailsByProductEntryId(String productEntryId)
            throws NotFoundException {
        List<ProductEntryDetail> productEntryDetails = productEntryDetailRepository
                .findByProductEntryId(productEntryId);
        if (productEntryDetails.isEmpty()) {
            throw new NotFoundException("No se encontraron detalles de la entrada de producto");
        }
        return productEntryDetails;
    }

}
