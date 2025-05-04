package com.ayd.employee_service.permissions.models;

import com.ayd.employee_service.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Permission extends Auditor {

    @Column(unique = true, length = 100)
    private String name;
    @Column(unique = true, length = 100)
    private String action;

    public Permission(String id) {
        super(id);
    }

    public Permission(String name, String action) {
        this.name = name;
        this.action = action;
    }

}
