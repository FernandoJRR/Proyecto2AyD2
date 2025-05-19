package com.ayd.employee_service.employees.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "paymentType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class PaymentType extends Auditor {
    @Column(length = 100)
    private String type;

    public PaymentType(String type) {
        this.type = type;
    }
}
