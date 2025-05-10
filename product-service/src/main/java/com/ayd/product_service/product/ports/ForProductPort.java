package com.ayd.product_service.product.ports;

import java.util.List;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.SpecificationProductDTO;
import com.ayd.product_service.product.dtos.StateProductResponseDTO;
import com.ayd.product_service.product.dtos.TypeProductResponseDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.models.Product;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

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
    public List<Product> getProductsByIds(List<String> ids);
    public List<StateProductResponseDTO> getStates();
    public List<TypeProductResponseDTO> getTypes();
}
