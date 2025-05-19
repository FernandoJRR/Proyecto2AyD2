package com.ayd.employee_service.employees.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ExtraPaymentResponseDTO {

    private String id;

    private PaymentTypeResponseDTO type;

    private String reason;

    private BigDecimal amount;

    private String description;

    private List<EmployeeResponseDTO> employees;
}
