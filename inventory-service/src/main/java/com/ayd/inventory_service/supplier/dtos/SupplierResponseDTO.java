package com.ayd.inventory_service.supplier.dtos;

import lombok.Value;
import java.math.BigDecimal;

@Value
public class SupplierResponseDTO {
    Long id;
    String nit;
    String name;
    BigDecimal taxRegime;
    String address;
    boolean active; 
}
