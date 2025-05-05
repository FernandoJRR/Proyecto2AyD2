package com.ayd.employee_service.employees.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayd.employee_service.employees.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

    public Optional<Employee> findByUser_Username(String username);

    @Query("SELECT e FROM Employee e JOIN e.vacations v " +
       "WHERE v.periodYear = :periodYear " +
       "GROUP BY e " +
       "HAVING COUNT(v) = SUM(CASE WHEN v.wasUsed = true THEN 1 ELSE 0 END)")
    public List<Employee> findEmployeesWithAllVacationsUsed(@Param("periodYear") Integer periodYear);
}
