package com.ayd.employee_service.employees.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;

@Value
public class EmployeeResponseDTO {

    String id;

    String cui;

    String firstName;

    String lastName;

    BigDecimal salary;

    BigDecimal igssPercentage;

    BigDecimal irtraPercentage;

    LocalDate desactivatedAt;

    EmployeeTypeResponseDTO employeeType;

}
