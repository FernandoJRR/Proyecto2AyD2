package com.ayd.employee_service.employees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.employee_service.employees.models.ExtraPayment;

public interface ExtraPaymentsRepository extends JpaRepository<ExtraPayment, String>{

}
