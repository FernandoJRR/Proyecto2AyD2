package com.ayd.employee_service.vacations.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ayd.employee_service.vacations.dtos.VacationPeriodRequestDTO;
import com.ayd.employee_service.vacations.dtos.VacationsResponseDTO;
import com.ayd.employee_service.vacations.models.Vacations;

@Mapper(componentModel = "spring")
public interface VacationsMapper {
    List<VacationsResponseDTO> fromVacationsListToVacationsResponseDTOs(List<Vacations> vacations);
    List<Vacations> fromVacationPeriodRequestToVacationsList(List<VacationPeriodRequestDTO> periodVacations);
    Vacations fromVacationPeriodRequestToVacations(VacationPeriodRequestDTO vacationPeriodRequestDTO);
    Map<Integer, List<VacationsResponseDTO>> fromVacationMapToVacationMapResponse(Map<Integer, List<Vacations>> vacations);
    VacationsResponseDTO fromVacationToVacationsResponseDTO(Vacations vacations);
}
