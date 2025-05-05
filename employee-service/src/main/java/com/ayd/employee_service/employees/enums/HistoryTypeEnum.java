package com.ayd.employee_service.employees.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryTypeEnum {
    CONTRATACION ("Contratacion"),
    DESPIDO ("Despido"),
    RENUNCIA ("Renuncia"),
    RECONTRATACION ("Recontratacion"),
    AUMENTO_SALARIAL ("Aumento Salarial"),
    DISMINUCION_SALARIAL ("Disminucion Salarial");

    private final String type;
}
