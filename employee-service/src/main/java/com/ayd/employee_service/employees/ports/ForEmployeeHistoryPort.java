package com.ayd.employee_service.employees.ports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.shared.exceptions.InvalidPeriodException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;

public interface ForEmployeeHistoryPort {
        public EmployeeHistory createEmployeeHistoryHiring(Employee employee, LocalDate hiringDate)
                        throws NotFoundException;

        public EmployeeHistory createEmployeeHistorySalaryIncrease(Employee employee, BigDecimal newSalary,
                        LocalDate date)
                        throws NotFoundException, InvalidPeriodException;

        public EmployeeHistory createEmployeeHistorySalaryDecrease(Employee employee, BigDecimal newSalary,
                        LocalDate date)
                        throws NotFoundException, InvalidPeriodException;

        public EmployeeHistory createEmployeeHistoryDeactivation(Employee employee, LocalDate deactivationDate,
                        HistoryType historyTypeReason)
                        throws NotFoundException, InvalidPeriodException;

        public EmployeeHistory createEmployeeHistoryReactivation(Employee employee, LocalDate deactivationDate)
                        throws NotFoundException, InvalidPeriodException;

        public List<EmployeeHistory> getEmployeeHistory(Employee employee) throws NotFoundException;

        public Optional<EmployeeHistory> getLastEmployeeSalaryUntilDate(Employee employee, LocalDate date)
                        throws NotFoundException;

        public Optional<EmployeeHistory> getMostRecentEmployeeSalary(Employee employee) throws NotFoundException;

        public Optional<EmployeeHistory> getEmployeeHiringDate(Employee employee) throws NotFoundException;

        public boolean isValidEmployeePeriod(Employee employee, LocalDate date);

        public boolean isValidEmployeePeriodDeactivationDate(Employee employee, LocalDate deactivationDate);

        public List<EmployeeHistory> getHistoriesByHistoryDateBetweenAndEmployeeTypeIdAndHistoryTypeIds(
                        LocalDate startDate,
                        LocalDate endDate,
                        String employeeTypeId,
                        List<String> historyTypeIds);
}
