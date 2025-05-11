package com.ayd.employee_service.employees.models;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "employeeType")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class EmployeeType extends Auditor {

    @Column(unique = true, length = 100)
    private String name;
    /**
     * Un tipo de empleado puede estar asignado a varios empleados
     */
    @OneToMany(mappedBy = "employeeType")
    private List<Employee> employees;

    /**
     * Un tipo de empleado tiene múltiples permisos y un permiso puede pertenecer a
     * varios tipos de empleados
     */
    @ManyToMany
    private List<Permission> permissions;

    public EmployeeType(String id, String name) {
        super(id);
        this.name = name;
    }

    public EmployeeType(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public EmployeeType(String name) {
        this.name = name;
    }
}
