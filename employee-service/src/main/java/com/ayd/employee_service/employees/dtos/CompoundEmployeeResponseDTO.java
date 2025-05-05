package com.ayd.employee_service.employees.dtos;

import java.util.List;
import java.util.Map;

import com.ayd.employee_service.vacations.dtos.VacationsResponseDTO;

import lombok.Value;

@Value
public class CompoundEmployeeResponseDTO {
    EmployeeResponseDTO employeeResponseDTO;
    String username;
    List<EmployeeHistoryResponseDTO> employeeHistories;
    Map<Integer, List<VacationsResponseDTO>> vacations;
}
