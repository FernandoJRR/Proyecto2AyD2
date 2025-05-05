package com.ayd.employee_service.employees.ports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.InvalidPeriodException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.models.User;

public interface ForEmployeesPort {

        public Employee createEmployee(Employee newEmployee, EmployeeType employeeType, User newUser,
                        EmployeeHistory employeeHistoryDate)
                        throws DuplicatedEntryException, NotFoundException;

        public Employee updateEmployee(String currentId, Employee newData, EmployeeType employeeType)
                        throws NotFoundException;

        public Employee updateEmployeeSalary(String currentId, BigDecimal newSalary, LocalDate salaryDate)
                        throws NotFoundException, InvalidPeriodException;

        public Employee reassignEmployeeType(String employeeId, String employeeTypeId) throws NotFoundException;

        public List<Employee> reassignEmployeeType(List<Employee> employeeIds, String employeeTypeId)
                        throws NotFoundException;

        public Employee findEmployeeById(String employeeId) throws NotFoundException;

        public Employee findEmployeeByUsername(String username) throws NotFoundException;

        public List<Employee> findEmployees();

        public List<Employee> findEmployeesInvoiceForPeriod(Integer periodYear);

        public Employee desactivateEmployee(String currentId, LocalDate deactivationDate, HistoryType historyTypeReason)
                        throws NotFoundException, IllegalStateException, InvalidPeriodException;

        public Employee reactivateEmployee(String currentId, LocalDate deactivationDate)
                        throws NotFoundException, IllegalStateException, InvalidPeriodException;

        public List<Employee> getEmployeesByType(String employeeTypeId, String search) throws NotFoundException;

        public List<Employee> getDoctors(String search) throws NotFoundException;

        public List<Employee> getNurses(String search) throws NotFoundException;

}
