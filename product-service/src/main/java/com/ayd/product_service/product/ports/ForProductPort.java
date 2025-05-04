package com.ayd.product_service.product.ports;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;

public interface ForProductPort {
    public void createProduct(CreateProductRequestDTO createProductRequestDTO);
    public void updateProduct(UpdateProductRequestDTO updateProductRequestDTO);
    public void deleteProduct(String id);
    public void getProduct(String id);
    public void getProducts();
}
