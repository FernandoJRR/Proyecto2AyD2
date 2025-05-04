package com.ayd.product_service.product.specifitacions;

import org.springframework.data.jpa.domain.Specification;

import com.ayd.product_service.product.models.Product;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCode(String code) {
        return (root, query, cb) ->
                code == null ? null : cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    public static Specification<Product> hasBarCode(String barCode) {
        return (root, query, cb) ->
                barCode == null ? null : cb.like(cb.lower(root.get("barCode")), "%" + barCode.toLowerCase() + "%");
    }

    public static Specification<Product> hasType(Integer type) {
        return (root, query, cb) ->
            type == null ? null : cb.equal(root.get("type"), type);
    }
    
    public static Specification<Product> hasState(Integer state) {
        return (root, query, cb) ->
            state == null ? null : cb.equal(root.get("state"), state);
    }
}
