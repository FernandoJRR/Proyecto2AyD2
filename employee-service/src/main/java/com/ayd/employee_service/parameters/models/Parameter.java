package com.ayd.employee_service.parameters.models;

import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Parameter extends Auditor {
    @Column(unique = true)
    private String parameterKey;

    @Column
    private String value;

    @Column
    private String name;
}