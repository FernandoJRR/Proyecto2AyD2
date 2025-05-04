package com.ayd.product_service.product.models;

import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;
import com.ayd.product_service.shared.models.Auditor;

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
    @Column(unique = true,nullable = false,length = 100)
    private String name;
    @Column(unique = true,nullable = true, length = 100)
    private String code;
    @Column(unique = true,nullable = true, length = 100)
    private String barCode;
    @Column(nullable = false)
    private EnumProductType type;
    @Column(nullable = false)
    private EnumProductState state;
}
