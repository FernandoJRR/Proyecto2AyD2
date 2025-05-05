package com.ayd.employee_service.employees.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.employee_service.shared.models.Auditor;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.vacations.models.Vacations;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Luis Monterroso
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class Employee extends Auditor {

    @Column(length = 100, unique = true, nullable = false)
    private String cui;
    @Column(length = 100)
    private String firstName;
    @Column(length = 100)
    private String lastName;
    @Column(scale = 2)
    private BigDecimal salary;
    @Column(precision = 5, scale = 2, nullable = true)
    private BigDecimal igssPercentage;
    @Column(precision = 5, scale = 2, nullable = true)
    private BigDecimal irtraPercentage;
    @Column(nullable = true)
    private LocalDate desactivatedAt;

    @ManyToOne
    private EmployeeType employeeType;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeHistory> employeeHistories;


    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacations> vacations;

    /**
     * Para la creacion de nuevos empleados
     *
     * @param firstName
     * @param lastName
     * @param salary
     * @param igssPercentage
     * @param irtraPercentage
     */
    public Employee(String cui, String firstName, String lastName, BigDecimal salary, BigDecimal igssPercentage,
            BigDecimal irtraPercentage) {
        super();
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.igssPercentage = igssPercentage;
        this.irtraPercentage = irtraPercentage;
    }

    public Employee(String cui, String firstName, String lastName, BigDecimal salary, BigDecimal igssPercentage,
            BigDecimal irtraPercentage, LocalDateTime resignDate, EmployeeType employeeType, User user) {
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.igssPercentage = igssPercentage;
        this.irtraPercentage = irtraPercentage;
        this.employeeType = employeeType;
        this.user = user;
    }

    public Employee(String id) {
        super(id);
    }

    public String getFullName() {
        // este format da un espacio entre el firtname y el lastname
        String fullname = String.format("%s %s", firstName, lastName);
        return fullname;
    }

}
