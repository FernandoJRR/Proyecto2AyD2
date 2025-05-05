package com.ayd.product_service.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.product_service.product.models.Product;

public interface ProductRepository extends JpaRepository<Product, String> , JpaSpecificationExecutor<Product> {

    public boolean existsByNameAndIdIsNot(String name, String id);
    public boolean existsByCodeAndIdIsNot(String code, String id);
    public boolean existsByBarCodeAndIdIsNot(String barCode, String id);
    public boolean existsByName(String name);
    public boolean existsByCode(String code);
    public boolean existsByBarCode(String barCode);
}
