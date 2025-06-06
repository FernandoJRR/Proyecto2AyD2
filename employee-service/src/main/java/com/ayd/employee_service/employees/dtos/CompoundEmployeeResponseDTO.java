package com.ayd.employee_service.employees.dtos;

import java.util.List;

import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;

import lombok.Value;

@Value
public class CompoundEmployeeResponseDTO {
    EmployeeResponseDTO employeeResponseDTO;
    String username;
    List<EmployeeHistoryResponseDTO> employeeHistories;
}
