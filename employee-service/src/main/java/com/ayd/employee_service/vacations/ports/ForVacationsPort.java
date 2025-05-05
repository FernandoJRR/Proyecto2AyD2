package com.ayd.employee_service.vacations.ports;

import java.util.List;
import java.util.Map;

import com.ayd.employee_service.shared.exceptions.InvalidPeriodException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.vacations.dtos.ChangeVacationDaysRequestDTO;
import com.ayd.employee_service.vacations.models.Vacations;

public interface ForVacationsPort {
    public List<Vacations> getAllVacationsForEmployeeOnPeriod(String employeeId, Integer period) throws NotFoundException;
    public Map<Integer, List<Vacations>> getAllVacationsForEmployee(String employeeId) throws NotFoundException;
    public List<Vacations> createVacationsForEmployeeOnPeriod(String employeeId, Integer period, List<Vacations> vacationsPeriods) throws NotFoundException, InvalidPeriodException;
    public Vacations changeVacationState(String vacationsId) throws NotFoundException, InvalidPeriodException;
    public List<Vacations> createRandomVacationsForEmployee(String employeeId) throws NotFoundException;
    public List<Vacations> updateVacationsForEmployeeOnPeriod(String employeeId, Integer period, List<Vacations> vacationsPeriods) throws NotFoundException, InvalidPeriodException;
    public Integer updateVacationDays(Integer newVacationDays) throws NotFoundException;
}
