package com.ayd.inventory_service.supplier.dtos;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class SpecificationSupplierRequestDTO {
    String nit;
    String name;
    BigDecimal taxRegime;
    String address;
    Boolean active;
}
