package com.ayd.inventory_service.warehouse.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ayd.inventory_service.warehouse.models.Warehouse;

public class WarehouseSpecification {

    public static Specification<Warehouse> hasName(String name) {
        return (root, query, cb) -> name == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Warehouse> hasUbication(String ubication) {
        return (root, query, cb) -> ubication == null ? null
                : cb.like(cb.lower(root.get("ubication")), "%" + ubication.toLowerCase() + "%");
    }

    public static Specification<Warehouse> isActive(Boolean active) {
        return (root, query, cb) -> active == null ? null : cb.equal(root.get("active"), active);
    }
}
