package com.ayd.employee_service.employees.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ayd.employee_service.employees.models.EmployeeHistory;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, String> {

    public List<EmployeeHistory> findAllByEmployee_IdOrderByHistoryDateAsc(String employeeId);

    public Optional<EmployeeHistory> findFirstByEmployee_IdAndHistoryType_IdInOrderByHistoryDateDesc(String employeeId,
            List<String> historyTypeIds);

    public Optional<EmployeeHistory> findFirstByEmployee_IdAndHistoryType_IdInAndHistoryDateLessThanEqualOrderByHistoryDateDesc(
            String employeeId, List<String> historyTypeIds, LocalDate untilDate);

    public Optional<EmployeeHistory> findFirstByEmployee_IdOrderByHistoryDateAsc(String employeeId);

    public List<EmployeeHistory> findByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateAsc(String employeeId,
            List<String> historyTypeTypes);

    public Optional<EmployeeHistory> findFirstByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateDesc(
            String employeeId,
            List<String> historyTypes);

    @Query("""
                SELECT eh FROM employeeHistory eh
                WHERE (:startDate IS NULL OR eh.historyDate >= :startDate)
                  AND (:endDate IS NULL OR eh.historyDate <= :endDate)
                  AND (:employeeTypeId IS NULL OR eh.employee.employeeType.id = :employeeTypeId)
                  AND (:historyTypeId IS NULL OR eh.historyType.id IN :historyTypeId)
                  ORDER BY eh.historyDate DESC
            """)
    public List<EmployeeHistory> findAllByHistoryDateBetweenAndEmployeeTypeIdAndHistoryTypeId(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("employeeTypeId") String employeeTypeId,
            @Param("historyTypeId") List<String> historyTypeIds);

}
