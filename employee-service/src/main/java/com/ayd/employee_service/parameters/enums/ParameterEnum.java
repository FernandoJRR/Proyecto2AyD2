package com.ayd.employee_service.parameters.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParameterEnum {
    DIAS_VACACIONES ("dias_vacaciones", "15", "Dias de Vacaciones")
    ;

    private final String key;
    private final String defaultValue;
    private final String name;
}
