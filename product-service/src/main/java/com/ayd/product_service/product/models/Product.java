package com.ayd.product_service.product.models;

import java.math.BigDecimal;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends Auditor {
    @Column(unique = true, nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = true, length = 100)
    private String code;
    @Column(unique = true, nullable = true, length = 100)
    private String barCode;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private EnumProductType type;
    @Column(nullable = false)
    private EnumProductState state;

    public Product(CreateProductRequestDTO createProductRequestDTO) {
        this.name = createProductRequestDTO.getName();
        this.code = createProductRequestDTO.getCode();
        this.barCode = createProductRequestDTO.getBarCode();
        this.type = createProductRequestDTO.getType();
        this.state = createProductRequestDTO.getState();
        this.price = createProductRequestDTO.getPrice();
    }

    public Product updateProduct(UpdateProductRequestDTO updateProductRequestDTO) {
        if (updateProductRequestDTO.getName() != null) {
            this.name = updateProductRequestDTO.getName();
        }
        if (updateProductRequestDTO.getCode() != null) {
            this.code = updateProductRequestDTO.getCode();
        }
        if (updateProductRequestDTO.getBarCode() != null) {
            this.barCode = updateProductRequestDTO.getBarCode();
        }
        if (updateProductRequestDTO.getType() != null) {
            this.type = updateProductRequestDTO.getType();
        }
        if (updateProductRequestDTO.getState() != null) {
            this.state = updateProductRequestDTO.getState();
        }
        if (updateProductRequestDTO.getPrice() != null) {
            this.price = updateProductRequestDTO.getPrice();
        }
        return this;
    }
}
