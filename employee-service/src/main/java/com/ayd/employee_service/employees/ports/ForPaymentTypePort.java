package com.ayd.employee_service.employees.ports;

import java.util.List;

import com.ayd.employee_service.employees.models.PaymentType;

public interface ForPaymentTypePort {
    public List<PaymentType> findAll();
}