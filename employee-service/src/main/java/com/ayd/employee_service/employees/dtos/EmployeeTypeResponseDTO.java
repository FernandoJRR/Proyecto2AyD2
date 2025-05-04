package com.ayd.employee_service.employees.dtos;

import java.util.List;

import com.ayd.employee_service.permissions.dtos.PermissionResponseDTO;

import lombok.Value;

@Value
public class EmployeeTypeResponseDTO {

    String id;
    String name;
    List<PermissionResponseDTO> permissions;
}
