package com.ayd.employee_service.employees.dtos;

import lombok.Value;

@Value
public class SpecialistEmpleoyeeResponseDTO {
    private String id;
    private String nombres;
    private String apellidos;
    private String dpi;
}
