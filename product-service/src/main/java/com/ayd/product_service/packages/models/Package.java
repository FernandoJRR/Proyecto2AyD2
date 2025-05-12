package com.ayd.product_service.packages.models;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.product_service.product.models.Product;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class Package extends Auditor {

    @ManyToMany
    private List<Product> products;

    @Column(nullable = false)
    private String name;
}
