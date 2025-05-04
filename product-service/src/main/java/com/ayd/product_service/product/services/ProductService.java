package com.ayd.product_service.product.services;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.SpecificationProductDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.ports.ForProductPort;
import com.ayd.product_service.product.repositories.ProductRepository;
import com.ayd.product_service.product.specifitacions.ProductSpecification;
import com.ayd.product_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.product_service.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProductService implements ForProductPort {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        if (productRepository.existsByName(createProductRequestDTO.getName())) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo nombre");
        }
        if (productRepository.existsByCode(createProductRequestDTO.getCode())) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo código");
        }
        if (productRepository.existsByBarCode(createProductRequestDTO.getBarCode())) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo código de barras");
        }
        Product product = new Product(createProductRequestDTO);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(String id, UpdateProductRequestDTO updateProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un producto con el id: " + id));
        if (productRepository.existsByNameAndIdIsNot(updateProductRequestDTO.getName(), id)) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo nombre");
        }
        if (productRepository.existsByCodeAndIdIsNot(updateProductRequestDTO.getCode(), id)) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo código");
        }
        if (productRepository.existsByBarCodeAndIdIsNot(updateProductRequestDTO.getBarCode(), id)) {
            throw new DuplicatedEntryException("Ya existe un producto con el mismo código de barras");
        }
        product.updateProduct(updateProductRequestDTO);
        productRepository.save(product);
        return product;
    }

    @Override
    public boolean deleteProduct(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un producto con el id: " + id));
        productRepository.delete(product);
        return true;
    }

    @Override
    public Product getProduct(String id) throws NotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un producto con el id: " + id));
    }

    @Override
    public List<Product> getProducts(SpecificationProductDTO specificationProductDTO) {
        Specification<Product> spec = Specification.
            where(ProductSpecification.hasName(specificationProductDTO.getName()))
            .and(ProductSpecification.hasCode(specificationProductDTO.getCode()))
            .and(ProductSpecification.hasBarCode(specificationProductDTO.getBarCode()))
            .and(ProductSpecification.hasType(specificationProductDTO.getType()))
            .and(ProductSpecification.hasState(specificationProductDTO.getState()));
        List<Product> products = productRepository.findAll(spec);
        return products;
    }
}
