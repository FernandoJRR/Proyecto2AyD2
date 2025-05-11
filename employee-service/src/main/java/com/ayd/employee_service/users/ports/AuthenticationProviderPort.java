package com.ayd.employee_service.users.ports;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.shared.exceptions.*;

public interface AuthenticationProviderPort {

    /**
     * Obtiene el empleado autenticado actualmente en el sistema.
     *
     * @return el objeto Employee correspondiente al usuario autenticado.
     * @throws IllegalStateException si no hay ningún usuario autenticado en el
     *                               contexto.
     * @throws NotFoundException     si no se encuentra un empleado asociado al
     *                               nombre de usuario autenticado.
     */
    public Employee getAutenticatedEmployee() throws IllegalStateException, NotFoundException;
}
