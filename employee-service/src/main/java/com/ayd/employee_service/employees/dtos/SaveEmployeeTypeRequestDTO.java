package com.ayd.employee_service.employees.dtos;

import java.util.List;

import com.ayd.employee_service.shared.dtos.IdRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SaveEmployeeTypeRequestDTO extends EmployeeTypeDTO {

    @NotNull(message = "La lista de permisos no puede ser nula.")
    @NotEmpty(message = "Debe proporcionar al menos un permiso.")
    private List<@Valid IdRequestDTO> permissions;

}
