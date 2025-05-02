package com.ayd.product_service.shared.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IdRequestDTO {

    @NotBlank(message = "El id no puede estar vacio.")
    private String id;
}
