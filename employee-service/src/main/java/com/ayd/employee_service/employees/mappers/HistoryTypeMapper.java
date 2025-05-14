package com.ayd.employee_service.employees.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.employee_service.employees.dtos.HistoryTypeResponseDTO;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.shared.dtos.IdRequestDTO;

@Mapper(componentModel = "spring")
public interface HistoryTypeMapper {
    HistoryTypeResponseDTO fromHistoryTypeToHistoryTypeResponseDTO(HistoryType historyType);
    HistoryType fromHistoryTypeDtoToHistoryType(HistoryTypeResponseDTO historyTypeResponseDTO);
    HistoryType fromIdRequestDtoToHistoryType(IdRequestDTO idRequestDTO);
    List<HistoryTypeResponseDTO> fromHistoryTypesToHistoryTypeResponseDTOs(List<HistoryType> historyTypes);
}
