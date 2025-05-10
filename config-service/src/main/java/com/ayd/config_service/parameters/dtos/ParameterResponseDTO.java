package com.ayd.config_service.parameters.dtos;

import lombok.Value;

@Value
public class ParameterResponseDTO {
    private String parameterKey;

    private String value;

    private String name;
}