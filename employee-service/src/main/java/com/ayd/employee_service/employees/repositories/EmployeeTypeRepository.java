package com.ayd.employee_service.employees.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.employee_service.employees.models.EmployeeType;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, String> {
    /**
     * Verifica si el nombre del tipo de empleado existe en la bd
     *
     * @param name
     * @return
     */
    public boolean existsByName(String name);

    public Optional<EmployeeType> findByName(String name);

    public boolean existsByNameAndIdIsNot(String name, String id);

    public long deleteEmployeeTypeById(String employeeTypeId);
}
