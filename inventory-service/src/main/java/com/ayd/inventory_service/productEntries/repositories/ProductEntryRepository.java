package com.ayd.inventory_service.productEntries.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.inventory_service.productEntries.models.ProductEntry;

public interface ProductEntryRepository extends JpaRepository<ProductEntry, String> {
    public boolean existsByInvoiceNumber(String invoiceNumber);

    public boolean existsByInvoiceNumberAndIdNot(String invoiceNumber, String id);
}
