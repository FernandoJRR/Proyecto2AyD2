package com.ayd.product_service.product.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.product_service.product.dtos.ProductResponseDTO;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.shared.utils.DateFormatterUtil;

@Mapper(componentModel = "spring", uses = { DateFormatterUtil.class })
public interface ProductMapper {

    public ProductResponseDTO fromProductToProductResponseDTO(Product product);
    
    public List<ProductResponseDTO> fromProductsToProductResponseDTOs(List<Product> products);
    
}
