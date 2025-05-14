package com.ayd.config_service.parameters.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParameterEnum {
    NOMBRE_EMPRESA ("nombre_empresa", "Golf Course", "Nombre de la Empresa"),
    NIT_EMPRESA ("nit_empresa", "17678426", "NIT de la Empresa"),
    REGIMEN_EMPRESA ("regimen_empresa", "{\"name\": \"peq\", \"value\": 5}", "Regimen Fiscal de la Empresa"),
    DIAS_VACACIONES ("dias_vacaciones", "15", "Dias de Vacaciones"),
    ;

    private final String key;
    private final String defaultValue;
    private final String name;
}
