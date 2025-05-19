package com.ayd.employee_service.employees.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    PAYMENT ("Pago"),
    DISCOUNT ("Descuento");

    private final String type;
}
