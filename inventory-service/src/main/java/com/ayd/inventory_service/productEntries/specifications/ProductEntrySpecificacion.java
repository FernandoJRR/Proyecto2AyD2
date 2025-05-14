package com.ayd.inventory_service.productEntries.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.ayd.inventory_service.productEntries.models.ProductEntry;

public class ProductEntrySpecificacion {
    public static Specification<ProductEntry> hasInvoiceNumber(String invoiceNumber) {
        return (root, query, cb) -> invoiceNumber == null ? null
                : cb.like(cb.lower(root.get("invoiceNumber")), "%" + invoiceNumber.toLowerCase() + "%");
    }

    public static Specification<ProductEntry> hasId(String id) {
        return (root, query, cb) -> id == null ? null
                : cb.equal(root.get("id"), id);
    }

    public static Specification<ProductEntry> hasWarehouseId(String warehouseId) {
        return (root, query, cb) -> warehouseId == null ? null
                : cb.equal(root.join("warehouse").get("id"), warehouseId);
    }

    public static Specification<ProductEntry> hasSupplierId(String supplierId) {
        return (root, query, cb) -> supplierId == null ? null
                : cb.equal(root.join("supplier").get("id"), supplierId);
    }

    public static Specification<ProductEntry> hasDate(LocalDate date) {
        return (root, query, cb) -> date == null ? null
                : cb.equal(root.get("date"), date);
    }
}
