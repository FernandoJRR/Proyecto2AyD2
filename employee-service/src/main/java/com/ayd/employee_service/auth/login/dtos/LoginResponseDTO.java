package com.ayd.employee_service.auth.login.dtos;

import java.util.List;

import com.ayd.employee_service.employees.dtos.EmployeeResponseDTO;
import com.ayd.employee_service.permissions.dtos.PermissionResponseDTO;

import lombok.Value;

@Value
public class LoginResponseDTO {

    String username;
    EmployeeResponseDTO employee;
    String token;
    List<PermissionResponseDTO> permissions;
}
