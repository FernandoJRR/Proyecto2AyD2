package com.ayd.config_service.parameters.mappers;

import org.mapstruct.Mapper;

import com.ayd.config_service.parameters.dtos.ParameterResponseDTO;
import com.ayd.config_service.parameters.models.Parameter;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    public ParameterResponseDTO fromParameterToResponse(Parameter parameter);
}
