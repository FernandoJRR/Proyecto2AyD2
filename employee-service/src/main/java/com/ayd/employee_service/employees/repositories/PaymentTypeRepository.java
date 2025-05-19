package com.ayd.employee_service.employees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.employee_service.employees.models.PaymentType;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, String> {
}
