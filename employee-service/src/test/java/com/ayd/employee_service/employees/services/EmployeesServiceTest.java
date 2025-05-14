package com.ayd.employee_service.employees.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForEmployeeHistoryPort;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.employees.ports.ForHistoryTypePort;
import com.ayd.employee_service.employees.repositories.EmployeeRepository;
import com.ayd.employee_service.shared.enums.EmployeeTypeEnum;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.InvalidPeriodException;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;
import com.ayd.employee_service.users.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeesServiceTest {
        @Mock
        private EmployeeRepository employeeRepository;
        @Mock
        private UserRepository userRepository;

        @Mock
        private ForEmployeeTypePort forEmployeeTypePort;

        @Mock
        private ForUsersPort forUsersPort;

        @Mock
        private ForEmployeeHistoryPort forEmployeeHistoryPort;

        @Mock
        private ForHistoryTypePort forHistoryTypePort;

        @InjectMocks
        private EmployeeService employeeService;

        private User user;
        private HistoryType historyType;
        private HistoryType historyTypeIncrease;
        private HistoryType historyTypeDecrease;
        private HistoryType historyTypeFiring;
        private EmployeeHistory employeeHistory;
        private EmployeeHistory reactivationHistory;
        private Employee employee;
        private Employee updatedEmployee;
        private EmployeeType employeeType;
        private EmployeeType employeeTypeDoctor;

        /** Para el nuevo empleado */
        private static final String CUI = "aafasdfaf231";
        private static final String EMPLOYEE_ID = "adsfgdh-arsgdfhg-adfgh";
        private static final String EMPLOYEE_FIRST_NAME = "Luis";
        private static final String EMPLOYEE_LAST_NAME = "Monterroso";
        private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal(1200);
        private static final BigDecimal EMPLOYEE_IGSS = new BigDecimal(10.2);
        private static final BigDecimal EMPLOYEE_IRTRA = new BigDecimal(10.2);

        /** Para el objeto usuario */
        private static final String USER_ID = "wqer-qwerw-qweq";
        private static final String USER_NAME = "Luis";
        private static final String USER_PASSWORD = "123";

        /** Para el objeto tipo de empleado */
        private static final String EMPLOYEE_TYPE_ID = "dasdd-asdasd-asdasd";
        private static final String EMPLOYEE_TYPE_ID_2 = "sdfg-sdfg-sdfg";

        /** Para actualizaciones */
        private static final String UPDATED_EMPLOYEE_CUI = "lkkdsdf";
        private static final String UPDATED_EMPLOYEE_FIRST_NAME = "Carlos";
        private static final String UPDATED_EMPLOYEE_LAST_NAME = "Ramírez";
        private static final BigDecimal UPDATED_EMPLOYEE_SALARY = new BigDecimal(7000);
        private static final BigDecimal UPDATED_EMPLOYEE_IGSS = new BigDecimal(5.25);
        private static final BigDecimal UPDATED_EMPLOYEE_IRTRA = new BigDecimal(10.2);

        /** Para el historial del empleado **/
        private static final String HISTORY_TYPE_ID = "fdsf-rtrer-bbvk";
        private static final String HISTORY_TYPE = "Contratacion";
        private static final String HISTORY_TYPE_ID_INCREASE = "rewp-fkds-bbvk";
        private static final String HISTORY_TYPE_INCREASE = "Aumento Salarial";
        private static final String HISTORY_TYPE_ID_DECREASE = "dflm-fodp-bbvk";
        private static final String HISTORY_TYPE_DECREASE = "Disminucion Salarial";
        private static final String HISTORY_TYPE_FIRING = "Despido";
        private static final String HISTORY_TYPE_ID_FIRING = "fdsj-ewoi-dsml";

        private static final String EMPLOYEE_HISTORY_ID = "rewf-fdsa-fdsd";
        private static final String EMPLOYEE_HISTORY_COMMENTARY = "Se realizo la contratacion con un salario de Q.7000";
        private static final LocalDate EMPLOYEE_HISTORY_LOCAL_DATE = LocalDate.of(2022, 11, 23);

        private static final String EMPLOYEE_REACTIVATION_HISTORY_ID = "klfd-dkns-fwsd";
        private static final String EMPLOYEE_REACTIVATION_HISTORY_COMMENTARY = "El empleado fue recontratado.";

        private static final LocalDate EMPLOYEE_REACTIVATION_LOCAL_DATE = LocalDate.of(2022, 3, 23);
        private static final LocalDate EMPLOYEE_DEACTIVATION_LOCAL_DATE = LocalDate.of(2022, 12, 23);
        private static final LocalDate EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE = LocalDate.of(2022, 1, 23);

        private static final BigDecimal EMPLOYEE_STARTING_SALARY = new BigDecimal(1200);
        private static final BigDecimal EMPLOYEE_NEW_SALARY = new BigDecimal(1500);
        private static final String EMPLOYEE_NEW_SALARY_COMMENTARY = "1500";
        private static final String EMPLOYEE_HISTORY_INCREASE_COMMENTARY = "Se realizo la contratacion con un salario de Q.1200";

        private static final BigDecimal EMPLOYEE_STARTING_DECREASE_SALARY = new BigDecimal(1500);
        private static final BigDecimal EMPLOYEE_NEW_SALARY_DECREASE = new BigDecimal(1200);
        private static final String EMPLOYEE_NEW_SALARY_DECREASE_COMMENTARY = "1200";
        private static final String EMPLOYEE_HISTORY_DECREASE_COMMENTARY = "Se realizo la contratacion con un salario de Q.1500";

        /**
         * este metodo se ejecuta antes de cualquier prueba individual, se hace para
         * inicializar los moks ademas del driver
         */
        @BeforeEach
        private void setUp() {
                employee = new Employee(CUI,
                                EMPLOYEE_FIRST_NAME,
                                EMPLOYEE_LAST_NAME,
                                EMPLOYEE_SALARY,
                                EMPLOYEE_IGSS,
                                EMPLOYEE_IRTRA);
                employee.setId(EMPLOYEE_ID);

                updatedEmployee = new Employee(UPDATED_EMPLOYEE_CUI,
                                UPDATED_EMPLOYEE_FIRST_NAME,
                                UPDATED_EMPLOYEE_LAST_NAME,
                                UPDATED_EMPLOYEE_SALARY,
                                UPDATED_EMPLOYEE_IGSS,
                                UPDATED_EMPLOYEE_IRTRA);

                user = new User(USER_ID, USER_NAME, USER_PASSWORD);

                historyType = new HistoryType(HISTORY_TYPE);
                historyType.setId(HISTORY_TYPE_ID);

                historyTypeIncrease = new HistoryType(HISTORY_TYPE_INCREASE);
                historyTypeIncrease.setId(HISTORY_TYPE_ID_INCREASE);

                historyTypeDecrease = new HistoryType(HISTORY_TYPE_DECREASE);
                historyTypeDecrease.setId(HISTORY_TYPE_ID_DECREASE);

                historyTypeFiring = new HistoryType(HISTORY_TYPE_FIRING);
                historyTypeFiring.setId(HISTORY_TYPE_ID_FIRING);

                employeeHistory = new EmployeeHistory(EMPLOYEE_HISTORY_COMMENTARY);
                employeeHistory.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE);
                employeeHistory.setId(EMPLOYEE_HISTORY_ID);

                reactivationHistory = new EmployeeHistory(EMPLOYEE_REACTIVATION_HISTORY_COMMENTARY);
                reactivationHistory.setHistoryDate(EMPLOYEE_REACTIVATION_LOCAL_DATE);
                reactivationHistory.setId(EMPLOYEE_REACTIVATION_HISTORY_ID);

                employeeType = new EmployeeType();
                employeeType.setId(EMPLOYEE_TYPE_ID);

                employeeTypeDoctor = new EmployeeType();
                employeeTypeDoctor.setId(EMPLOYEE_TYPE_ID_2);
                employeeTypeDoctor.setName(EmployeeTypeEnum.DOCTOR.name());

                // inicializamos los empleados que usaremos para la reasignacion del tipo de
                // empleado
                employeeToReasignEmployeeType1 = new Employee(EMPLOYEE_ID_1);
                employeeToReasignEmployeeType2 = new Employee(EMPLOYEE_ID_2);
        }

        @Test
        public void shouldInsertEmployee() throws DuplicatedEntryException, NotFoundException {

                // ARRANGE
                // configuramos el mock para que lance el user cuando este sea creado
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);
                when(forUsersPort.createUser(any())).thenReturn(user);
                when(employeeRepository.save(any())).thenReturn(employee);
                when(forUsersPort.createUser(any(User.class))).thenReturn(user);
                when(forEmployeeHistoryPort.createEmployeeHistoryHiring(any(Employee.class), any(LocalDate.class)))
                                .thenReturn(employeeHistory);
                when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
                // ACT
                Employee result = employeeService.createEmployee(employee, employeeType, user, employeeHistory);

                // ASSERT
                // captor para capturar el objeto pasado a save()
                ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
                // verificar que se llamó a save() y capturar el argumento
                verify(employeeRepository).save(employeeCaptor.capture());
                // Obtener el objeto capturado
                Employee capturedEmployee = employeeCaptor.getValue();

                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(result.getFirstName(), capturedEmployee.getFirstName()),
                                () -> assertEquals(result.getLastName(), capturedEmployee.getLastName()),
                                () -> assertEquals(result.getSalary(), capturedEmployee.getSalary()),
                                () -> assertEquals(user, capturedEmployee.getUser()),
                                () -> assertEquals(employeeHistory, capturedEmployee.getEmployeeHistories().get(0)),
                                () -> assertEquals(employeeType, capturedEmployee.getEmployeeType())

                );

                // se verifican las llamadas a los métodos dependientes
                verify(forUsersPort, times(1)).createUser(any(User.class));
                verify(forEmployeeHistoryPort, times(1)).createEmployeeHistoryHiring(any(Employee.class),
                                any(LocalDate.class));
                verify(employeeRepository, times(1)).save(any(Employee.class));
        }

        @Test
        public void shouldNotInsertEmployeeWithExistantUsername() throws DuplicatedEntryException, NotFoundException {

                // ARRANGE
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);
                when(forUsersPort.createUser(user)).thenThrow(DuplicatedEntryException.class);

                // ACT and Asserts
                assertThrows(DuplicatedEntryException.class, () -> {
                        // se verifica que se haya lanzado la excepcion
                        employeeService.createEmployee(employee, employeeType, user, employeeHistory);
                });

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(forUsersPort, times(1)).createUser(any(User.class));
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void shouldNotInsertEmployeeWithInexistantEmployeeType()
                        throws NotFoundException, DuplicatedEntryException {
                // ARRANGE
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenThrow(
                                NotFoundException.class);

                // ACT
                assertThrows(NotFoundException.class, () -> {
                        // se verifica que se haya lanzado la excepcion
                        employeeService.createEmployee(employee, employeeType, user, employeeHistory);
                });

                // Asserts
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(forUsersPort, times(0)).createUser(any(User.class));
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void updateEmployeeShouldUpdateFieldsAndSave() throws NotFoundException {

                // ARRANGE
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);
                when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

                // ACT
                Employee updateEmployee = employeeService.updateEmployee(employee.getId(), updatedEmployee,
                                employeeType);

                // validar que los setters fueron llamados con los valores correctos
                assertAll(
                                () -> assertEquals(UPDATED_EMPLOYEE_FIRST_NAME, updateEmployee.getFirstName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_FIRST_NAME, updateEmployee.getFirstName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_LAST_NAME, updateEmployee.getLastName()),
                                () -> assertEquals(UPDATED_EMPLOYEE_SALARY, updateEmployee.getSalary()),
                                () -> assertEquals(UPDATED_EMPLOYEE_IGSS, updateEmployee.getIgssPercentage()),
                                () -> assertEquals(UPDATED_EMPLOYEE_IRTRA, updateEmployee.getIrtraPercentage()),
                                () -> assertEquals(employeeType, updateEmployee.getEmployeeType()));

                // Asegurar que se guardó en la base de datos
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(employeeRepository, times(1)).save(any(Employee.class));

        }

        @Test
        public void updateEmployeeShouldNotUpdateEmployeeWithInexistantEmployee() throws NotFoundException {
                // ARRANGE
                // cuando se busque el empleado por id entonces volvr vacio para que lance la
                // excepcion
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.updateEmployee(anyString(), employee, employeeType);
                });

                // Asserts
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(0)).existsEmployeeTypeById(anyString());
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void updateEmployeeShouldNotUpdateEmployeeWithInexistantEmployeeType() throws NotFoundException {
                // ARRANGE
                // cuando se busque por id entonces devolver el employee
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));

                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenThrow(
                                NotFoundException.class);
                // ACT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.updateEmployee(anyString(), employee, employeeType);
                });

                // ASSERTS
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
                verify(employeeRepository, times(0)).save(employee);

        }

        @Test
        public void shouldReactivateEmployeeSuccessfully()
                        throws NotFoundException, IllegalStateException, InvalidPeriodException {
                // ARRANGE
                employee.setUser(user);
                employee.setDesactivatedAt(EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE);
                user.setDesactivatedAt(EMPLOYEE_OLD_DEACTIVATION_LOCAL_DATE);
                employee.setEmployeeHistories(new ArrayList<>());

                when(employeeRepository.findById(eq(EMPLOYEE_ID))).thenReturn(Optional.of(employee));

                when(forEmployeeHistoryPort.createEmployeeHistoryReactivation(employee,
                                EMPLOYEE_REACTIVATION_LOCAL_DATE))
                                .thenReturn(reactivationHistory);
                when(forEmployeeHistoryPort.createEmployeeHistoryReactivation(employee,
                                EMPLOYEE_REACTIVATION_LOCAL_DATE))
                                .thenReturn(reactivationHistory);

                when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // ACT
                Employee reactivatedEmployee = employeeService.reactivateEmployee(EMPLOYEE_ID,
                                EMPLOYEE_REACTIVATION_LOCAL_DATE);

                // ASSERT
                ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
                verify(employeeRepository).save(employeeCaptor.capture());
                Employee capturedEmployee = employeeCaptor.getValue();

                assertAll(
                                () -> assertNotNull(reactivatedEmployee, "Reactivated employee should not be null"),
                                () -> assertEquals(null, capturedEmployee.getDesactivatedAt(),
                                                "Employee deactivation date should be null after reactivation"),
                                () -> assertEquals(null, capturedEmployee.getUser().getDesactivatedAt(),
                                                "User deactivation date should be null after reactivation"),
                                () -> assertEquals(1, capturedEmployee.getEmployeeHistories().size(),
                                                "There should be one history record added"),
                                () -> assertEquals(reactivationHistory, capturedEmployee.getEmployeeHistories().get(0),
                                                "The reactivation history record should be added"));
        }

        @Test
        public void testDesactivateEmployee() throws NotFoundException, IllegalStateException, InvalidPeriodException {
                // ARRANGE
                HistoryType reason = new HistoryType("Motivo de Desactivación");
                reason.setId("reason-id-123");

                employee.setUser(user);
                employee.setEmployeeHistories(new ArrayList<>());

                when(employeeRepository.findById(eq(EMPLOYEE_ID))).thenReturn(Optional.of(employee));

                EmployeeHistory deactivationHistory = new EmployeeHistory("Empleado desactivado.");
                deactivationHistory.setHistoryDate(EMPLOYEE_DEACTIVATION_LOCAL_DATE);
                when(forEmployeeHistoryPort.createEmployeeHistoryDeactivation(employee,
                                EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                reason))
                                .thenReturn(deactivationHistory);

                when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // ACT
                Employee deactivatedEmployee = employeeService.desactivateEmployee(EMPLOYEE_ID,
                                EMPLOYEE_DEACTIVATION_LOCAL_DATE, reason);

                // ASSERT
                ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
                verify(employeeRepository).save(employeeCaptor.capture());
                Employee capturedEmployee = employeeCaptor.getValue();

                assertAll(
                                () -> assertNotNull(deactivatedEmployee, "The returned employee should not be null"),
                                () -> assertEquals(EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                capturedEmployee.getDesactivatedAt(),
                                                "Employee deactivation date should match"),
                                () -> assertEquals(EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                capturedEmployee.getUser().getDesactivatedAt(),
                                                "User deactivation date should match"),
                                () -> assertEquals(1, capturedEmployee.getEmployeeHistories().size(),
                                                "Employee histories should contain one record"),
                                () -> assertEquals(deactivationHistory, capturedEmployee.getEmployeeHistories().get(0),
                                                "The deactivation history record should be added"));
        }

        /**
         * dado: que el empleado ya está desactivado en el sistema.
         * cuando: se intenta desactivar nuevamente.
         * entonces: se lanza una excepción IllegalStateException y no se realizan
         * cambios.
         */
        @Test
        public void desactivateEmployeeShouldThrowIllegalStateExceptionWhenEmployeeIsAlreadyDeactivated()
                        throws NotFoundException {
                // ARRANGE
                employee.setUser(user); // aseguramos que el empleado tenga un usuario
                // hacemos que el usuario ya este desactivado
                employee.setDesactivatedAt(LocalDate.now());
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));

                // ACT y ASSERT
                assertThrows(IllegalStateException.class,
                                () -> {
                                        employeeService.desactivateEmployee(EMPLOYEE_ID,
                                                        EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                                        any(HistoryType.class));
                                });
        }

        @Test
        public void desactivateEmployeeShouldNotDesactivateEmployeeWithInexistantEmployee() throws NotFoundException {
                // ARRANGE
                // cuando se busque por id mandamos vacio para que se lance la excepcion
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT
                // Asserts
                assertThrows(NotFoundException.class, () -> {
                        employeeService.desactivateEmployee(anyString(), EMPLOYEE_DEACTIVATION_LOCAL_DATE,
                                        historyTypeFiring);
                });
        }

        @Test
        public void shouldUpdateEmployeeSalaryForIncrease() throws NotFoundException, InvalidPeriodException {

                // ARRANGE
                List<EmployeeHistory> histories = new ArrayList<>();

                EmployeeHistory increaseHistory = new EmployeeHistory(EMPLOYEE_HISTORY_INCREASE_COMMENTARY);
                increaseHistory.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE);
                increaseHistory.setHistoryType(historyTypeIncrease);

                histories.add(increaseHistory);

                employee.setEmployeeHistories(histories);
                employee.setSalary(EMPLOYEE_STARTING_SALARY);

                LocalDate salaryDate = LocalDate.now();
                BigDecimal newSalary = EMPLOYEE_NEW_SALARY;

                // se crea el registro de un aumento salarial en el historial del empleado
                EmployeeHistory salaryIncreaseHistory = new EmployeeHistory(EMPLOYEE_NEW_SALARY_COMMENTARY);
                salaryIncreaseHistory.setHistoryDate(salaryDate);

                // se hace que el ultimo salario del empleado este vacio
                when(forEmployeeHistoryPort.getLastEmployeeSalaryUntilDate(employee, salaryDate))
                                .thenReturn(Optional.empty());

                when(forEmployeeHistoryPort.createEmployeeHistorySalaryIncrease(employee, newSalary, salaryDate))
                                .thenReturn(salaryIncreaseHistory);

                // se retorna el ultimo cambio de salario
                when(forEmployeeHistoryPort.getMostRecentEmployeeSalary(employee))
                                .thenReturn(Optional.of(salaryIncreaseHistory));

                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                when(employeeRepository.save(employee)).thenReturn(employee);

                // ACT
                Employee updatedEmployee = employeeService.updateEmployeeSalary(employee.getId(), newSalary,
                                salaryDate);

                // ASSERT
                assertAll(
                                () -> assertEquals(newSalary, updatedEmployee.getSalary()),
                                () -> assertEquals(2, updatedEmployee.getEmployeeHistories().size()),
                                () -> assertEquals(salaryIncreaseHistory,
                                                updatedEmployee.getEmployeeHistories().get(1)));

                verify(forEmployeeHistoryPort, times(1))
                                .createEmployeeHistorySalaryIncrease(employee, newSalary, salaryDate);
                verify(forEmployeeHistoryPort, times(1)).getMostRecentEmployeeSalary(employee);
                verify(employeeRepository, times(1)).save(employee);
        }

        @Test
        public void shouldUpdateEmployeeSalaryForDecrease() throws NotFoundException, InvalidPeriodException {

                // ARRANGE
                List<EmployeeHistory> histories = new ArrayList<>();

                EmployeeHistory decreaseHistory = new EmployeeHistory(EMPLOYEE_HISTORY_DECREASE_COMMENTARY);
                decreaseHistory.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE);
                decreaseHistory.setHistoryType(historyTypeDecrease);

                histories.add(decreaseHistory);

                employee.setEmployeeHistories(histories);
                employee.setSalary(EMPLOYEE_STARTING_DECREASE_SALARY);

                LocalDate salaryDate = LocalDate.now();
                BigDecimal newSalary = EMPLOYEE_NEW_SALARY_DECREASE;

                // se crea el registro de un aumento salarial en el historial del empleado
                EmployeeHistory salaryDecreaseHistory = new EmployeeHistory(EMPLOYEE_NEW_SALARY_DECREASE_COMMENTARY);
                salaryDecreaseHistory.setHistoryDate(salaryDate);

                // se hace que el ultimo salario del empleado este vacio
                when(forEmployeeHistoryPort.getLastEmployeeSalaryUntilDate(employee, salaryDate))
                                .thenReturn(Optional.empty());

                when(forEmployeeHistoryPort.createEmployeeHistorySalaryDecrease(employee, newSalary, salaryDate))
                                .thenReturn(salaryDecreaseHistory);

                // se retorna el ultimo cambio de salario
                when(forEmployeeHistoryPort.getMostRecentEmployeeSalary(employee))
                                .thenReturn(Optional.of(salaryDecreaseHistory));

                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                when(employeeRepository.save(employee)).thenReturn(employee);

                // ACT
                Employee updatedEmployee = employeeService.updateEmployeeSalary(employee.getId(), newSalary,
                                salaryDate);

                // ASSERT
                assertAll(
                                () -> assertEquals(newSalary, updatedEmployee.getSalary()),
                                () -> assertEquals(2, updatedEmployee.getEmployeeHistories().size()),
                                () -> assertEquals(salaryDecreaseHistory,
                                                updatedEmployee.getEmployeeHistories().get(1)));

                verify(forEmployeeHistoryPort, times(1))
                                .createEmployeeHistorySalaryDecrease(employee, newSalary, salaryDate);
                verify(forEmployeeHistoryPort, times(1)).getMostRecentEmployeeSalary(employee);
                verify(employeeRepository, times(1)).save(employee);
        }

        /**
         * dado: que el empleado y el tipo de empleado existen en la base de datos.
         * cuando: se reasigna el tipo de empleado a un nuevo tipo válido.
         * entonces: el empleado debe tener actualizado el nuevo tipo de empleado.
         */
        @Test
        public void reassignEmployeeTypeShouldReassignEmployeeTypeSuccessfully() throws NotFoundException {

                // ARRANGE
                // cuando se busque por el id entonoces devolver nuestro mock de empleado
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                // cuando se buswque e tipo de empleado a asignar entonces devolver nuestro mock
                when(forEmployeeTypePort.findEmployeeTypeById(anyString())).thenReturn(employeeType);

                // ACT
                Employee result = employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(employeeType, result.getEmployeeType()));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que el empleado no existe en la base de datos.
         * cuando: se intenta reasignar su tipo de empleado.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan
         * cambios.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeDoesNotExist() throws NotFoundException {
                // ARRANGE
                // al devolver el empty en el findBi entonces el metodo debe lanzar un not found
                when(employeeRepository.findById(anyString())).thenReturn(Optional.empty());

                // ACT &y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, never()).findEmployeeTypeById(anyString());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta reasignar el empleado a ese tipo de empleado inexistente.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan
         * cambios.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeTypeDoesNotExist() throws NotFoundException {
                // ARRANGE
                // si deolvemos el optional lleno
                when(employeeRepository.findById(anyString())).thenReturn(Optional.of(employee));
                // cuando mandemos a busqcar el tpio de empleado a asignar simulamos que este no
                // se encontro
                when(forEmployeeTypePort.findEmployeeTypeById(anyString()))
                                .thenThrow(new NotFoundException(anyString()));

                // ACT y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(EMPLOYEE_ID, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(1)).findById(EMPLOYEE_ID);
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);

        }

        private static final String EMPLOYEE_ID_1 = "sdaf-asdf-sad";
        private static final String EMPLOYEE_ID_2 = "";

        Employee employeeToReasignEmployeeType1;
        Employee employeeToReasignEmployeeType2;

        /**
         * dado: que una lista de empleados y un tipo de empleado existen en la base de
         * datos.
         * cuando: se reasignan todos los empleados a un nuevo tipo de empleado válido.
         * entonces: todos los empleados deben tener actualizado el nuevo tipo de
         * empleado.
         */
        @Test
        public void shouldReassignEmployeeTypeForMultipleEmployeesSuccessfully() throws NotFoundException {
                // ARRANGE
                List<Employee> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));
                when(employeeRepository.findById(EMPLOYEE_ID_2))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType2));
                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID)).thenReturn(employeeType);

                // ACT
                List<Employee> result = employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(2, result.size()),
                                () -> assertEquals(employeeType, result.get(0).getEmployeeType()),
                                () -> assertEquals(employeeType, result.get(1).getEmployeeType()));

                verify(employeeRepository, times(2)).findById(anyString());
                verify(forEmployeeTypePort, times(2)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que al menos un empleado de la lista no existe en la base de datos.
         * cuando: se intenta reasignar su tipo de empleado.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan cambios
         * en ningún empleado.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenAnyEmployeeDoesNotExist() throws NotFoundException {
                // ARRANGE
                List<Employee> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));
                // cuando busquemos el segundo id entonces devolvemos un Optional vacio para
                // forzar el NotFound
                when(employeeRepository.findById(EMPLOYEE_ID_2)).thenReturn(Optional.empty());

                // ACT yASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID));

                verify(employeeRepository, times(2)).findById(anyString());
                // solo se debra hacer realizado una busqueda del tipo de empledo porque a la
                // segunda vez ya habra fallado antes de llegar alli
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(anyString());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta reasignar una lista de empleados a ese tipo de empleado
         * inexistente.
         * entonces: se lanza una excepción `NotFoundException` y no se realizan cambios
         * en ningún empleado.
         *
         * @throws NotFoundException
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeTypeDoesNotExistForMultipleEmployees()
                        throws NotFoundException {
                // ARRANGE
                List<Employee> employees = List.of(employeeToReasignEmployeeType1, employeeToReasignEmployeeType2);

                when(employeeRepository.findById(EMPLOYEE_ID_1))
                                .thenReturn(Optional.of(employeeToReasignEmployeeType1));

                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID))
                                .thenThrow(new NotFoundException(anyString()));

                // ACT y ASSERT
                assertThrows(NotFoundException.class,
                                () -> employeeService.reassignEmployeeType(employees, EMPLOYEE_TYPE_ID));

                // solo se debera realzar una vez porque fallara el buscar el tipo de empleado
                verify(employeeRepository, times(1)).findById(anyString());
                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
        }

        /**
         * dado: que existen empleados en la base de datos.
         * cuando: se consulta la lista de empleados.
         * entonces: el método devuelve una lista con los empleados existentes.
         */
        @Test
        public void shouldReturnListOfEmployeesWhenEmployeesExist() {
                // ARRANGE
                List<Employee> employees = List.of(employee, updatedEmployee);
                when(employeeRepository.findAll()).thenReturn(employees);

                // ACT
                List<Employee> result = employeeService.findEmployees();

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(2, result.size()));

                verify(employeeRepository, times(1)).findAll();
        }

        /**
         * dado: que existe un tipo de empleado válido en la base de datos.
         * cuando: se busca a los empleados por ese tipo y se proporciona un término de
         * búsqueda.
         * entonces: se devuelve una lista con los empleados que coinciden con el nombre
         * o apellido buscado.
         */
        @Test
        public void shouldReturnEmployeesByTypeWithMatchingSearch() throws NotFoundException {
                // ARRANGE
                String search = "Luis";
                List<Employee> expectedEmployees = List.of(employee);

                when(forEmployeeTypePort.findEmployeeTypeById(EMPLOYEE_TYPE_ID)).thenReturn(employeeType);
                when(employeeRepository.findAll(ArgumentMatchers.<Specification<Employee>>any()))
                                .thenReturn(expectedEmployees);

                // ACT
                List<Employee> result = employeeService.getEmployeesByType(EMPLOYEE_TYPE_ID, search);

                // ASSERT
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(1, result.size()),
                                () -> assertEquals(employee.getFirstName(), result.get(0).getFirstName()));

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById(EMPLOYEE_TYPE_ID);
                verify(employeeRepository, times(1)).findAll(ArgumentMatchers.<Specification<Employee>>any());
        }

        /**
         * dado: que el tipo de empleado no existe en la base de datos.
         * cuando: se intenta obtener empleados por ese tipo.
         * entonces: se lanza una excepción `NotFoundException` y no se realiza ninguna
         * consulta a la base de datos.
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenTypeNotFoundInGetEmployeesByType() throws NotFoundException {
                // ARRANGE
                when(forEmployeeTypePort.findEmployeeTypeById(anyString()))
                                .thenThrow(new NotFoundException("Tipo de empleado no encontrado"));

                // ACT & ASSERT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.getEmployeesByType("invalid-id", "Luis");
                });

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeById("invalid-id");
                verify(employeeRepository, never()).findAll(ArgumentMatchers.<Specification<Employee>>any());
        }

        /**
         * dado: que el tipo de empleado "Doctor" no existe en la base de datos.
         * cuando: se intenta obtener doctores con un término de búsqueda.
         * entonces: se lanza una excepción `NotFoundException` y no se consulta el
         * repositorio.
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenDoctorTypeNotFound() throws NotFoundException {
                // ARRANGE
                when(forEmployeeTypePort.findEmployeeTypeByName(EmployeeTypeEnum.DOCTOR.name()))
                                .thenThrow(new NotFoundException("Tipo Doctor no encontrado"));

                // ACT & ASSERT
                assertThrows(NotFoundException.class, () -> {
                        employeeService.getDoctors("Luis");
                });

                verify(forEmployeeTypePort, times(1)).findEmployeeTypeByName(EmployeeTypeEnum.DOCTOR.name());
                verify(employeeRepository, never()).findAll(ArgumentMatchers.<Specification<Employee>>any());
        }

        /**
         * dado: que existe un empleado registrado con el nombre de usuario
         * proporcionado.
         * cuando: se busca el empleado por su nombre de usuario.
         * entonces: se retorna el empleado correspondiente sin lanzar ninguna
         * excepción.
         */
        @Test
        public void shouldReturnEmployeeWhenEmployeeExistsByUsername() throws NotFoundException {
                // arrange
                when(employeeRepository.findByUser_Username(anyString())).thenReturn(Optional.of(employee));

                // act
                Employee result = employeeService.findEmployeeByUsername(USER_NAME);

                // assert
                assertAll(
                                () -> assertNotNull(result),
                                () -> assertEquals(EMPLOYEE_ID, result.getId()),
                                () -> assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName()));
        }

        /**
         * dado: que no existe un empleado registrado con el nombre de usuario
         * proporcionado.
         * cuando: se intenta buscar el empleado por su nombre de usuario.
         * entonces: se lanza una excepción NotFoundException indicando que no fue
         * encontrado.
         */
        @Test
        public void shouldThrowNotFoundExceptionWhenEmployeeNotFoundByUsername() {
                // arrange
                when(employeeRepository.findByUser_Username(anyString())).thenReturn(Optional.empty());

                // act y assert
                assertThrows(
                                NotFoundException.class,
                                () -> employeeService.findEmployeeByUsername(USER_NAME));

        }

}
