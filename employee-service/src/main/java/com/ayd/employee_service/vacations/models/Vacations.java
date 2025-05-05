package com.ayd.employee_service.vacations.models;

import java.time.LocalDate;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Vacations extends Auditor {
    @Column
    private Integer periodYear;

    @ManyToOne
    @JoinColumn
    private Employee employee;

    @Column
    private LocalDate beginDate;

    @Column
    private LocalDate endDate;

    @Column
    private Integer workingDays;

    @Column
    private Boolean wasUsed;

    public Vacations(LocalDate beginDate, LocalDate endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}