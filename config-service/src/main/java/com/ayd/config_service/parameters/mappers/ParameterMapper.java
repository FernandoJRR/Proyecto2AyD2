package com.ayd.config_service.parameters.mappers;

import org.mapstruct.Mapper;

import com.ayd.config_service.parameters.dtos.ParameterDiasRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterNITRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterNombreRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterRegimenRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterResponseDTO;
import com.ayd.config_service.parameters.models.Parameter;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    public ParameterResponseDTO fromParameterToResponse(Parameter parameter);
    public String fromRegimenRequestToString(ParameterRegimenRequestDTO request);
    public String fromNITRequestToString(ParameterNITRequestDTO request);
    public String fromNombreRequestToString(ParameterNombreRequestDTO request);
    public String fromDiasRequestToString(ParameterDiasRequestDTO request);
}
