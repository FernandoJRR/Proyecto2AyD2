package com.ayd.employee_service.employees.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.employee_service.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "historyType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class HistoryType extends Auditor {
    @Column(length = 100)
    private String type;

    public HistoryType(String type) {
        this.type = type;
    }
}
