package com.ayd.sharedConfigService.dto;

import lombok.Value;

@Value
public class ParameterResponseDTO {
    private String parameterKey;

    private String value;

    private String name;
}
