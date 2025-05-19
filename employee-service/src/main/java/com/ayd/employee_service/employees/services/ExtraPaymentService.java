package com.ayd.employee_service.employees.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.employee_service.employees.dtos.CreateExtraPaymentDTO;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.ExtraPayment;
import com.ayd.employee_service.employees.models.PaymentType;
import com.ayd.employee_service.employees.ports.ForExtraPaymentsPort;
import com.ayd.employee_service.employees.repositories.EmployeeRepository;
import com.ayd.employee_service.employees.repositories.ExtraPaymentsRepository;
import com.ayd.employee_service.employees.repositories.PaymentTypeRepository;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExtraPaymentService implements ForExtraPaymentsPort {
    private final PaymentTypeRepository paymentTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final ExtraPaymentsRepository extraPaymentsRepository;

    public ExtraPayment createExtraPayment(CreateExtraPaymentDTO request) throws NotFoundException {
        PaymentType type = paymentTypeRepository.findById(request.getPaymentTypeId())
                .orElseThrow(() -> new NotFoundException("Tipo de extra no encontrado"));

        List<Employee> employees = employeeRepository.findAllById(request.getEmployeesIds());
        if (employees.size() != request.getEmployeesIds().size()) {
            throw new NotFoundException("Uno o más empleados no fueron encontrados");
        }

        ExtraPayment extraPayment = new ExtraPayment();
        extraPayment.setType(type);
        extraPayment.setReason(request.getReason());
        extraPayment.setAmount(request.getAmount());
        extraPayment.setDescription(request.getDescription());
        extraPayment.setEmployees(employees);

        return extraPaymentsRepository.save(extraPayment);
    }

    public List<ExtraPayment> getAllExtraPayments() {
        return extraPaymentsRepository.findAll();
    }

    public ExtraPayment getPaymentById(String paymentId) throws NotFoundException {
        return extraPaymentsRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("El extra no fue encontrado"));
    }
}
