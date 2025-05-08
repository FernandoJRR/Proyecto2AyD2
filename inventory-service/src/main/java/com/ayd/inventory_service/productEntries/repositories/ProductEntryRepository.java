package com.ayd.inventory_service.productEntries.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.inventory_service.productEntries.models.ProductEntry;

public interface ProductEntryRepository extends JpaRepository<ProductEntry, String>,JpaSpecificationExecutor<ProductEntry> {
    public boolean existsByInvoiceNumber(String invoiceNumber);

    public boolean existsByInvoiceNumberAndIdNot(String invoiceNumber, String id);

    public Optional<ProductEntry> findByInvoiceNumber(String invoiceNumber);
}
