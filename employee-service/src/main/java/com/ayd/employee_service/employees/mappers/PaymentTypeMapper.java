package com.ayd.employee_service.employees.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.employee_service.employees.dtos.PaymentTypeResponseDTO;
import com.ayd.employee_service.employees.models.PaymentType;

@Mapper(componentModel = "spring")
public interface PaymentTypeMapper {
    PaymentTypeResponseDTO fromPaymentTypeResponseDTO(PaymentType paymentType);
    List<PaymentTypeResponseDTO> fromPaymentTypesToResponseDTO(List<PaymentType> paymentTypes);
}
