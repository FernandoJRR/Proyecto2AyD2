package com.ayd.employee_service.shared.enums;

import com.ayd.employee_service.employees.models.EmployeeType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que define los tipos de empleados predefinidos en el sistema.
 */
@Getter
@AllArgsConstructor
public enum EmployeeTypeEnum {

    DEFAULT(new EmployeeType("Sin Asignar")),
    ADMIN(new EmployeeType("Admin")),
    DOCTOR(new EmployeeType("Doctor")),
    ENFERMERO(new EmployeeType("Enfermero")),
    FARMACEUTICO(new EmployeeType("Farmaceutico"));

    private final EmployeeType employeeType;
}
