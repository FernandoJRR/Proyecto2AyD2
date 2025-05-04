package com.ayd.product_service.product.services;

import org.springframework.stereotype.Service;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.ports.ForProductPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProductService implements ForProductPort {@Override
    
    public void createProduct(CreateProductRequestDTO createProductRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProduct'");
    }

    @Override
    public void updateProduct(UpdateProductRequestDTO updateProductRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public void deleteProduct(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

    @Override
    public void getProduct(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProduct'");
    }

    @Override
    public void getProducts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProducts'");
    }
    
}
