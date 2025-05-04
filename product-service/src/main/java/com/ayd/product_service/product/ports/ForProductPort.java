package com.ayd.product_service.product.ports;

import java.util.List;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.SpecificationProductDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.product_service.shared.exceptions.NotFoundException;

public interface ForProductPort {
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException;
    public Product updateProduct(String id, UpdateProductRequestDTO updateProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException;
    public boolean deleteProduct(String id)
            throws NotFoundException;
    public Product getProduct(String id)
            throws NotFoundException;
    public List<Product> getProducts(SpecificationProductDTO specificationProductDTO);
}
