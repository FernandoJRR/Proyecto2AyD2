package com.ayd.employee_service.employees.models;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "employeeHistory")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class EmployeeHistory extends Auditor {
    @ManyToOne
    private HistoryType historyType;

    @ManyToOne
    private Employee employee;

    @Column(length = 200)
    private String commentary;

    @Column(nullable = true)
    private LocalDate historyDate;

    public EmployeeHistory(String commentary) {
        this.commentary = commentary;
    }
}
