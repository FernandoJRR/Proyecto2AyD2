package com.ayd.employee_service.employees.models;

import com.ayd.employee_service.permissions.models.Permission;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTypeTest {

    private static final String ID = "dasfgh-asdfgg";
    private static final String NAME = "ADMIN";
    private static final Permission PERMISSION = new Permission("EDIT", "EDITAR");
    private static final List<Permission> PERMISSIONS = List.of(PERMISSION);

    /**
     * dado: un ID y un nombre.
     * cuando: se instancia un tipo de empleado con esos datos.
     * entonces: se inicializan correctamente el ID y el nombre, y los demás campos
     * permanecen nulos.
     */
    @Test
    public void shouldCreateEmployeeTypeWithIdAndName() {
        // arrange y act
        EmployeeType employeeType = new EmployeeType(ID, NAME);

        // assert
        assertAll(
                () -> assertEquals(ID, employeeType.getId()),
                () -> assertEquals(NAME, employeeType.getName()),
                () -> assertNull(employeeType.getPermissions()),
                () -> assertNull(employeeType.getEmployees()));
    }

    /**
     * dado: un nombre y una lista de permisos.
     * cuando: se instancia un tipo de empleado con esos parámetros.
     * entonces: se inicializan correctamente el nombre y los permisos, y los demás
     * campos permanecen nulos.
     */
    @Test
    public void shouldCreateEmployeeTypeWithNameAndPermissions() {
        // arrange y act
        EmployeeType employeeType = new EmployeeType(NAME, PERMISSIONS);

        // assert
        assertAll(
                () -> assertEquals(NAME, employeeType.getName()),
                () -> assertEquals(PERMISSIONS, employeeType.getPermissions()),
                () -> assertNull(employeeType.getEmployees()),
                () -> assertNull(employeeType.getId()));
    }

    /**
     * dado: solo un nombre de tipo de empleado.
     * cuando: se instancia con el constructor que solo recibe el nombre.
     * entonces: se inicializa el nombre correctamente y los demás campos permanecen
     * nulos.
     */
    @Test
    public void shouldCreateEmployeeTypeWithNameOnly() {
        // arrange y act
        EmployeeType employeeType = new EmployeeType(NAME);

        // assert
        assertAll(
                () -> assertEquals(NAME, employeeType.getName()),
                () -> assertNull(employeeType.getPermissions()),
                () -> assertNull(employeeType.getEmployees()),
                () -> assertNull(employeeType.getId()));
    }
}
