package com.ayd.inventory_service.productEntries.ports;

import java.util.List;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;
import com.ayd.shared.exceptions.*;

public interface ForProductEntryDetailPort {
    public ProductEntryDetail getProductEntryDetailById(String id)
            throws NotFoundException;

    public ProductEntryDetail saveProductEntryDetail(ProductEntryDetailRequestDTO productEntryDetailRequestDTO,
            ProductEntry productEntry)
            throws NotFoundException;

    public List<ProductEntryDetail> getAllProductEntryDetailsByProductEntryId(String productEntryId)
            throws NotFoundException;
}
