package com.ayd.product_service.packages.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.product_service.product.models.Product;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class GolfPackageDetail extends Auditor {

    @ManyToOne
    @JoinColumn(nullable = false)
    private GolfPackage packagee;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Product product;

}
