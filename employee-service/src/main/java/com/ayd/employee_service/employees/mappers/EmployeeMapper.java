package com.ayd.employee_service.employees.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.employee_service.employees.dtos.CreateEmployeeRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeRequestDTO;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    public Employee fromCreateEmployeeRequestDtoToEmployee(CreateEmployeeRequestDTO dto);

    public Employee fromEmployeeRequestDtoToEmployee(EmployeeRequestDTO dto);

    public EmployeeResponseDTO fromEmployeeToResponse(Employee employee);

    public List<EmployeeResponseDTO> fromEmployeesToResponse(List<Employee> employees);

}
