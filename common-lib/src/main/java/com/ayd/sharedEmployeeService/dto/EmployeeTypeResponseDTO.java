package com.ayd.sharedEmployeeService.dto;

import java.util.List;

import lombok.Value;

@Value
public class EmployeeTypeResponseDTO {

    String id;
    String name;
    List<PermissionResponseDTO> permissions;
}
