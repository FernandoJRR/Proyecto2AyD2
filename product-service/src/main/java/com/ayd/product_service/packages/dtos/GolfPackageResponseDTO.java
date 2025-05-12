package com.ayd.product_service.packages.dtos;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;

@Value
public class GolfPackageResponseDTO {

    String id;

    String name;

    String description;

    BigDecimal price;

    Boolean active;

    List<GolfPackageDetailResponseDTO> packageDetail;

}
