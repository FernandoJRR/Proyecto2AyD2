package com.ayd.employee_service.auth.login.dtos;

import java.util.List;

import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.ayd.sharedEmployeeService.dto.PermissionResponseDTO;

import lombok.Value;

@Value
public class LoginResponseDTO {

    String username;
    EmployeeResponseDTO employee;
    String token;
    List<PermissionResponseDTO> permissions;
}
