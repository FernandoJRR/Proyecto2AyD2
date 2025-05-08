package com.ayd.inventory_service.productEntries.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayd.inventory_service.productEntries.models.ProductEntryDetail;

public interface ProductEntryDetailRepository extends JpaRepository<ProductEntryDetail, String> {
    public List<ProductEntryDetail> findByProductId(String productId);
    public List<ProductEntryDetail> findByProductEntryId(String productEntryId);

    @Query("SELECT COALESCE(SUM(p.unitPrice * p.quantity), 0) FROM ProductEntryDetail p WHERE p.productEntry.id = :productEntryId")
    BigDecimal sumTotalByProductEntryId(@Param("productEntryId") String productEntryId);
}
