package com.ayd.product_service.product.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.product_service.product.models.Product;
import com.ayd.sharedProductService.product.dtos.ProductResponseDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    public ProductResponseDTO fromProductToProductResponseDTO(Product product);

    public List<ProductResponseDTO> fromProductsToProductResponseDTOs(List<Product> products);

}
