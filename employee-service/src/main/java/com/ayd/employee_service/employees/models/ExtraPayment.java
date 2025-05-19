package com.ayd.employee_service.employees.models;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class ExtraPayment extends Auditor {
    @ManyToOne
    private PaymentType type;

    @Column(scale = 2)
    private BigDecimal amount;

    @Column
    private String reason;

    @Column
    private String description;

    @ManyToMany
    private List<Employee> employees;
}
