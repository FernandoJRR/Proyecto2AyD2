package com.ayd.inventory_service.supplier.specifications;

import com.ayd.inventory_service.supplier.models.Supplier;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class SupplierSpecification {

    public static Specification<Supplier> hasNit(String nit) {
        return (root, query, cb) -> nit == null ? null
                : cb.like(cb.lower(root.get("nit")), "%" + nit.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasTaxRegime(BigDecimal taxRegime) {
        return (root, query, cb) -> taxRegime == null ? null
                : cb.equal(root.get("taxRegime"), taxRegime);
    }

    public static Specification<Supplier> hasAddress(String address) {
        return (root, query, cb) -> address == null ? null
                : cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Supplier> isActive(Boolean active) {
        return (root, query, cb) -> active == null ? null
                : cb.equal(root.get("active"), active);
    }
}
