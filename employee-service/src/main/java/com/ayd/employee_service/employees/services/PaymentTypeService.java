package com.ayd.employee_service.employees.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.employee_service.employees.models.PaymentType;
import com.ayd.employee_service.employees.ports.ForPaymentTypePort;
import com.ayd.employee_service.employees.repositories.PaymentTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PaymentTypeService implements ForPaymentTypePort {
    private final PaymentTypeRepository paymentTypeRepository;

    public List<PaymentType> findAll(){
        return paymentTypeRepository.findAll();
    }
}
