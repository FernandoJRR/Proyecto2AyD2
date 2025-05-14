package com.ayd.config_service.parameters.mappers;

import org.mapstruct.Mapper;

import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    public ParameterResponseDTO fromParameterToResponse(Parameter parameter);
}
