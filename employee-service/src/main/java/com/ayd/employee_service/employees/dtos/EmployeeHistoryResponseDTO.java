package com.ayd.employee_service.employees.dtos;

import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;

import lombok.Value;

@Value
public class EmployeeHistoryResponseDTO {

    EmployeeResponseDTO employee;

    HistoryTypeResponseDTO historyType;

    String commentary;

    String historyDate;
}
