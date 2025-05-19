package com.ayd.employee_service.employees.ports;

import java.util.List;

import com.ayd.employee_service.employees.dtos.CreateExtraPaymentDTO;
import com.ayd.employee_service.employees.models.ExtraPayment;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForExtraPaymentsPort {
    public ExtraPayment createExtraPayment(CreateExtraPaymentDTO request) throws NotFoundException;
    public List<ExtraPayment> getAllExtraPayments();
    public ExtraPayment getPaymentById(String paymentId) throws NotFoundException;
}
