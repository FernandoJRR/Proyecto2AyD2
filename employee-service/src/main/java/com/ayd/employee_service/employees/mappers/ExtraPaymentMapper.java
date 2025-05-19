package com.ayd.employee_service.employees.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.employee_service.employees.dtos.ExtraPaymentResponseDTO;
import com.ayd.employee_service.employees.models.ExtraPayment;

@Mapper(componentModel = "spring")
public interface ExtraPaymentMapper {
    ExtraPaymentResponseDTO fromExtraPaymentToResponseDTO(ExtraPayment extraPayment);
    List<ExtraPaymentResponseDTO> fromExtraPaymentsToResponseDTO(List<ExtraPayment> extraPayments);
}
