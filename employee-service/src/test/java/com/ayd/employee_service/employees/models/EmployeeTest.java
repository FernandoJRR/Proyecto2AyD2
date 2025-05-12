package com.ayd.employee_service.employees.models;

import com.ayd.employee_service.users.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private static final String CUI = "2345330910901";
    private static final String FIRST_NAME = "Luis";
    private static final String LAST_NAME = "Monterroso";
    private static final BigDecimal SALARY = new BigDecimal("5000.00");
    private static final BigDecimal IGSS = new BigDecimal("4.83");
    private static final BigDecimal IRTRA = new BigDecimal("1.25");
    private static final LocalDateTime RESIGN_DATE = LocalDateTime.now();
    private static final String EMPLOYEE_ID = "abc-123";

    private static final String EMPLOYEE_TYPE_ID = "idEmployeeTYpe";
    private static final String EMPLOYEE_TYPE_NAME = "idEmployeeTYpe";

    private static final String USER = "username";
    private static final String PASSWORD = "password";

    private EmployeeType employeeType;
    private User user;

    @BeforeEach
    public void setUp() {
        employeeType = new EmployeeType(EMPLOYEE_TYPE_ID, EMPLOYEE_TYPE_NAME);
        user = new User(USER, PASSWORD);
    }

    /**
     * dado: datos válidos para crear un empleado (nombre, apellido, salario,
     * porcentajes y fecha de renuncia).
     * cuando: se instancia un empleado usando el constructor principal sin ID ni
     * tipo ni usuario.
     * entonces: se inicializan correctamente los campos proporcionados y los demás
     * permanecen nulos.
     */
    @Test
    public void shouldCreateEmployeeWithMainConstructorWithoutId() {
        // arrange y act
        Employee employee = new Employee(CUI, FIRST_NAME, LAST_NAME, SALARY, IGSS, IRTRA);
        // assert
        assertAll(
                () -> assertEquals(CUI, employee.getCui()),
                () -> assertEquals(FIRST_NAME, employee.getFirstName()),
                () -> assertEquals(LAST_NAME, employee.getLastName()),
                () -> assertEquals(SALARY, employee.getSalary()),
                () -> assertEquals(IGSS, employee.getIgssPercentage()),
                () -> assertEquals(IRTRA, employee.getIrtraPercentage()),
                () -> assertNull(employee.getEmployeeType()),
                () -> assertNull(employee.getUser()),
                () -> assertNull(employee.getId()));
    }

    /**
     * dado: datos válidos incluyendo tipo de empleado y usuario.
     * cuando: se instancia un empleado usando el constructor completo (excepto ID).
     * entonces: se inicializan correctamente todos los campos proporcionados y el
     * ID permanece nulo.
     */
    @Test
    public void shouldCreateEmployeeWithAllFieldsConstructorExeptId() {
        // arrange y act
        Employee employee = new Employee(CUI, FIRST_NAME, LAST_NAME, SALARY, IGSS, IRTRA, RESIGN_DATE, employeeType,
                user);
        // asert
        assertAll(
                () -> assertEquals(CUI, employee.getCui()),
                () -> assertEquals(FIRST_NAME, employee.getFirstName()),
                () -> assertEquals(LAST_NAME, employee.getLastName()),
                () -> assertEquals(SALARY, employee.getSalary()),
                () -> assertEquals(IGSS, employee.getIgssPercentage()),
                () -> assertEquals(IRTRA, employee.getIrtraPercentage()),
                () -> assertEquals(employeeType, employee.getEmployeeType()),
                () -> assertEquals(user, employee.getUser()),
                () -> assertNull(employee.getId()));

    }

    /**
     * dado: un empleado instanciado con el constructor que omite el campo ID.
     * cuando: se invoca el método getFullName().
     * entonces: se retorna correctamente el nombre completo concatenando nombre y
     * apellido.
     */
    @Test
    public void shouldReturnFullNameWhenEmployeeIsCreatedWithConstructorWithoutId() {
        // arrange
        Employee employee = new Employee(CUI, FIRST_NAME, LAST_NAME, SALARY, IGSS, IRTRA, RESIGN_DATE, employeeType,
                user);
        String expectedFullname = String.format("%s %s", FIRST_NAME, LAST_NAME);

        String result = employee.getFullName();

        // asert
        assertAll(
                () -> assertEquals(expectedFullname, result));

    }

    /**
     * dado: un ID específico.
     * cuando: se instancia un empleado solo con ese ID.
     * entonces: se inicializa únicamente el ID y todos los demás campos quedan
     * nulos.
     */
    @Test
    public void shouldCreateEmployeeWithIdOnly() {
        // arrange y act
        Employee employee = new Employee(EMPLOYEE_ID);
        // assert
        assertAll(
                () -> assertEquals(EMPLOYEE_ID, employee.getId()),
                () -> assertNull(employee.getFirstName()),
                () -> assertNull(employee.getLastName()),
                () -> assertNull(employee.getSalary()),
                () -> assertNull(employee.getIgssPercentage()),
                () -> assertNull(employee.getIrtraPercentage()),
                () -> assertNull(employee.getDesactivatedAt()),
                () -> assertNull(employee.getEmployeeType()),
                () -> assertNull(employee.getUser()));
    }
}
