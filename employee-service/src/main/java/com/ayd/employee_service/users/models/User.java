/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ayd.employee_service.users.models;

import java.time.LocalDate;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends Auditor {

    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 255)
    private String password;

    private LocalDate desactivatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Employee employee;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

}
