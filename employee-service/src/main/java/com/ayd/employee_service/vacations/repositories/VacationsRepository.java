package com.ayd.employee_service.vacations.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ayd.employee_service.vacations.models.Vacations;

import jakarta.transaction.Transactional;

public interface VacationsRepository extends JpaRepository<Vacations, String>{
    public List<Vacations> findAllByEmployee_IdAndPeriodYearOrderByBeginDateAsc(String employeeId, Integer periodYear);
    public List<Vacations> findAllByEmployee_IdOrderByBeginDateAsc(String employeeId);
    public List<Vacations> findAllByEmployee_IdAndPeriodYearAndWasUsedTrue(String employeeId, Integer periodYear);
    public boolean existsByEmployee_IdAndPeriodYear(String employeeId, Integer periodYear);

    @Modifying
    @Transactional
    public void deleteByEmployee_IdAndPeriodYear(String employeeId, Integer periodYear);

}
