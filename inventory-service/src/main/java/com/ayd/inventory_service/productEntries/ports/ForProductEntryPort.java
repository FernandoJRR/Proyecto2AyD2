package com.ayd.inventory_service.productEntries.ports;

import java.util.List;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryRequestDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntrySpecificationDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.shared.exceptions.*;

public interface ForProductEntryPort {
    public ProductEntry getProductEntryById(String id)
            throws NotFoundException;

    public ProductEntry getProductEntryByInvoiceNumber(String invoiceNumber)
            throws NotFoundException;

    public ProductEntry saveProductEntry(ProductEntryRequestDTO productEntryRequestDTO)
            throws NotFoundException, DuplicatedEntryException,IllegalStateException;

    public List<ProductEntry> getAllProductEntries();

    public List<ProductEntry> getAlByProductEntrieSpecification(ProductEntrySpecificationDTO productEntrySpecificationDTO);
}
