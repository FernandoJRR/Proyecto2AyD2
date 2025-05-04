package com.ayd.product_service.product.dtos;

import java.util.List;

import lombok.Value;


@Value
public class GetProductsByIdsRequestDTO {
    private List<String> ids;

}
