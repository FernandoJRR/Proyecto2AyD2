package com.ayd.employee_service.employees.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ayd.employee_service.employees.enums.HistoryTypeEnum;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForHistoryTypePort;
import com.ayd.employee_service.employees.repositories.EmployeeHistoryRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.InvalidPeriodException;
import com.ayd.shared.exceptions.NotFoundException;

public class EmployeeHistoryServiceTest {

    @Mock
    private EmployeeHistoryRepository employeeHistoryRepository;

    @Mock
    private ForHistoryTypePort forHistoryTypePort;

    @InjectMocks
    private EmployeeHistoryService employeeHistoryService;

    private Employee employee;
    private EmployeeHistory employeeHistoryHiring;
    private EmployeeHistory employeeHistoryIncrease;
    private EmployeeHistory employeeHistoryDecrease;
    private HistoryType historyType;
    private HistoryType historyTypeIncrease;
    private HistoryType historyTypeDecrease;
    private HistoryType deactivationReason;
    private HistoryType historyTypeDeactivationDummy;
    private HistoryType reactivationHistoryType;
    private EmployeeHistory employeeHistoryLastDeactivation;

    private static final String HISTORY_TYPE_ID = "fdsf-rtrer-bbvk";
    private static final String HISTORY_TYPE = "Contratacion";
    private static final LocalDate EMPLOYEE_HISTORY_LOCAL_DATE = LocalDate.of(2022, 11, 23);
    private static final LocalDate EMPLOYEE_HISTORY_LOCAL_DATE_INCREASE = LocalDate.of(2022, 12, 23);
    private static final LocalDate EMPLOYEE_HISTORY_LOCAL_DATE_DECREASE = LocalDate.of(2022, 12, 23);

    // Constantes para el empleado de test
    private static final String EMPLOYEE_ID = "adsfgdh-arsgdfhg-adfgh";
    private static final String CUI = "3214356432";
    private static final String EMPLOYEE_FIRST_NAME = "Fernando";
    private static final String EMPLOYEE_LAST_NAME = "Rodriguez";
    private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal(1200);
    private static final BigDecimal EMPLOYEE_IGSS = new BigDecimal(10.2);
    private static final BigDecimal EMPLOYEE_IRTRA = new BigDecimal(10.2);

    private static final String EMPLOYEE_HISTORY_HIRING_COMMENTARY = "Se realizo la contratacion con un salario de Q.1200";
    private static final String EMPLOYEE_HISTORY_ID = "fdsf-jhds-jtes";
    private static final String EMPLOYEE_HISTORY_ID_INCREASE = "lkjf-fsdm-pdfp";
    private static final String EMPLOYEE_HISTORY_ID_DECREASE = "renp-codn-fdos";

    private static final String HISTORY_TYPE_ID_INCREASE = "rewp-fkds-bbvk";
    private static final String HISTORY_TYPE_INCREASE = "Aumento Salarial";
    private static final String HISTORY_TYPE_ID_DECREASE = "dflm-fodp-bbvk";
    private static final String HISTORY_TYPE_DECREASE = "Disminucion Salarial";
    private static final String HISTORY_TYPE_ID_REACTIVATION = "sdll-fjis-mkdc";
    private static final String HISTORY_TYPE_REACTIVATION = "Empleado recontratado.";

    private static final LocalDate EMPLOYEE_REACTIVATION_DATE = LocalDate.of(2023, 2, 1);

    private static final String EMPLOYEE_NEW_SALARY = "1500";

    private static final LocalDate EMPLOYEE_DEACTIVATION_LOCAL_DATE = LocalDate.of(2022, 12, 23);

    private static final String HISTORY_TYPE_FIRING = "Despido";
    private static final String HISTORY_TYPE_ID_FIRING = "fdsj-ewoi-dsml";

    private static final String EMPLOYEE_HISTORY_FIRING_COMMENTARY = "Se despidio al empleado.";
    private static final LocalDate EMPLOYEE_HISTORY_FIRING_LOCAL_DATE = LocalDate.of(2022, 12, 1);

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee(CUI,
                EMPLOYEE_FIRST_NAME,
                EMPLOYEE_LAST_NAME,
                EMPLOYEE_SALARY,
                EMPLOYEE_IGSS,
                EMPLOYEE_IRTRA);
        employee.setId(EMPLOYEE_ID);

        historyType = new HistoryType(HISTORY_TYPE);
        historyType.setId(HISTORY_TYPE_ID);

        historyTypeDecrease = new HistoryType(HISTORY_TYPE_DECREASE);
        historyTypeDecrease.setId(HISTORY_TYPE_ID_DECREASE);

        historyTypeIncrease = new HistoryType(HISTORY_TYPE_INCREASE);
        historyTypeIncrease.setId(HISTORY_TYPE_ID_INCREASE);

        deactivationReason = new HistoryType(HISTORY_TYPE_FIRING);
        deactivationReason.setId(HISTORY_TYPE_ID_FIRING);

        historyTypeDeactivationDummy = new HistoryType(HISTORY_TYPE_FIRING);
        historyTypeDeactivationDummy.setId(HISTORY_TYPE_ID_FIRING);

        employeeHistoryHiring = new EmployeeHistory(EMPLOYEE_HISTORY_HIRING_COMMENTARY);
        employeeHistoryHiring.setHistoryType(historyType);
        employeeHistoryHiring.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE);
        employeeHistoryHiring.setId(EMPLOYEE_HISTORY_ID);

        employeeHistoryIncrease = new EmployeeHistory(EMPLOYEE_HISTORY_HIRING_COMMENTARY);
        employeeHistoryIncrease.setHistoryType(historyTypeIncrease);
        employeeHistoryIncrease.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE_INCREASE);
        employeeHistoryIncrease.setId(EMPLOYEE_HISTORY_ID_INCREASE);

        employeeHistoryDecrease = new EmployeeHistory(EMPLOYEE_HISTORY_HIRING_COMMENTARY);
        employeeHistoryDecrease.setHistoryType(historyTypeDecrease);
        employeeHistoryDecrease.setHistoryDate(EMPLOYEE_HISTORY_LOCAL_DATE_DECREASE);
        employeeHistoryDecrease.setId(EMPLOYEE_HISTORY_ID_DECREASE);

        employeeHistoryLastDeactivation = new EmployeeHistory(EMPLOYEE_HISTORY_FIRING_COMMENTARY);
        employeeHistoryLastDeactivation.setHistoryDate(EMPLOYEE_HISTORY_FIRING_LOCAL_DATE);
        employeeHistoryLastDeactivation.setHistoryType(historyTypeDeactivationDummy);

        reactivationHistoryType = new HistoryType(HISTORY_TYPE_REACTIVATION);
        reactivationHistoryType.setId(HISTORY_TYPE_ID_REACTIVATION);

    }

    @Test
    public void shouldCreateHiringEmployeeHistory() throws DuplicatedEntryException, NotFoundException {
        // arrglar
        when(forHistoryTypePort.findHistoryTypeByName(HISTORY_TYPE)).thenReturn(historyType);
        when(employeeHistoryRepository.save(any(EmployeeHistory.class))).thenReturn(employeeHistoryHiring);

        // actuar
        EmployeeHistory resultEmployeeHistory = employeeHistoryService.createEmployeeHistoryHiring(employee,
                EMPLOYEE_HISTORY_LOCAL_DATE);

        // evaluar
        ArgumentCaptor<EmployeeHistory> employeeHistoryCaptor = ArgumentCaptor.forClass(EmployeeHistory.class);

        // se captura lo guardado por save()
        verify(employeeHistoryRepository).save(employeeHistoryCaptor.capture());

        EmployeeHistory capturedEmployeeHistory = employeeHistoryCaptor.getValue();

        assertAll(
                () -> assertNotNull(resultEmployeeHistory),
                () -> assertEquals(resultEmployeeHistory.getCommentary(), capturedEmployeeHistory.getCommentary()),
                () -> assertEquals(resultEmployeeHistory.getHistoryDate(), capturedEmployeeHistory.getHistoryDate()),
                () -> assertEquals(resultEmployeeHistory.getHistoryType(), capturedEmployeeHistory.getHistoryType()));
    }

    @Test
    public void shouldCreateSalaryIncreaseHistory() throws NotFoundException, InvalidPeriodException {
        // ARRANGE
        BigDecimal newSalary = new BigDecimal(EMPLOYEE_NEW_SALARY);

        when(employeeHistoryRepository.findByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateAsc(
                eq(employee.getId()), anyList()))
                .thenReturn(List.of(employeeHistoryHiring));

        when(forHistoryTypePort.findHistoryTypeByName(HISTORY_TYPE_INCREASE)).thenReturn(historyTypeIncrease);

        when(employeeHistoryRepository.save(any(EmployeeHistory.class)))
                .thenReturn(employeeHistoryIncrease);

        // ACT
        EmployeeHistory resultEmployeeHistory = employeeHistoryService
                .createEmployeeHistorySalaryIncrease(employee, newSalary, EMPLOYEE_HISTORY_LOCAL_DATE_INCREASE);

        // ASSERT
        ArgumentCaptor<EmployeeHistory> employeeHistoryCaptor = ArgumentCaptor.forClass(EmployeeHistory.class);
        verify(employeeHistoryRepository).save(employeeHistoryCaptor.capture());
        EmployeeHistory capturedEmployeeHistory = employeeHistoryCaptor.getValue();

        assertAll(
                () -> assertNotNull(resultEmployeeHistory),
                () -> assertEquals(newSalary.toString(), capturedEmployeeHistory.getCommentary()),
                () -> assertEquals(EMPLOYEE_HISTORY_LOCAL_DATE_INCREASE, capturedEmployeeHistory.getHistoryDate()),
                () -> assertEquals(historyTypeIncrease, capturedEmployeeHistory.getHistoryType()),
                () -> assertEquals(employee, capturedEmployeeHistory.getEmployee()));
    }

    @Test
    public void shouldThrowInvalidPeriodExceptionWhenPeriodIsInvalidOnSalaryIncrease() {
        BigDecimal newSalary = new BigDecimal(EMPLOYEE_NEW_SALARY);
        LocalDate increaseDate = EMPLOYEE_HISTORY_LOCAL_DATE;

        when(employeeHistoryRepository.findByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateAsc(
                eq(employee.getId()), anyList()))
                .thenReturn(List.of());

        // ASSERT
        assertThrows(InvalidPeriodException.class, () -> {
            employeeHistoryService.createEmployeeHistorySalaryIncrease(employee, newSalary, increaseDate);
        });
    }

    @Test
    public void shouldCreateSalaryDecreaseHistory() throws NotFoundException, InvalidPeriodException {
        // ARRANGE
        BigDecimal newSalary = new BigDecimal(EMPLOYEE_NEW_SALARY);
        LocalDate decreaseDate = EMPLOYEE_HISTORY_LOCAL_DATE_DECREASE;

        when(employeeHistoryRepository.findByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateAsc(
                eq(employee.getId()), anyList()))
                .thenReturn(List.of(employeeHistoryHiring));

        when(forHistoryTypePort.findHistoryTypeByName(HISTORY_TYPE_DECREASE)).thenReturn(historyTypeDecrease);
        when(employeeHistoryRepository.save(any(EmployeeHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        EmployeeHistory resultEmployeeHistory = employeeHistoryService
                .createEmployeeHistorySalaryDecrease(employee, newSalary, decreaseDate);

        // ASSERT
        ArgumentCaptor<EmployeeHistory> employeeHistoryCaptor = ArgumentCaptor.forClass(EmployeeHistory.class);
        verify(employeeHistoryRepository).save(employeeHistoryCaptor.capture());
        EmployeeHistory capturedEmployeeHistory = employeeHistoryCaptor.getValue();

        assertAll(
                () -> assertNotNull(resultEmployeeHistory),
                () -> assertEquals(newSalary.toString(), capturedEmployeeHistory.getCommentary()),
                () -> assertEquals(decreaseDate, capturedEmployeeHistory.getHistoryDate()),
                () -> assertEquals(historyTypeDecrease, capturedEmployeeHistory.getHistoryType()),
                () -> assertEquals(employee, capturedEmployeeHistory.getEmployee()));
    }

    @Test
    public void shouldThrowInvalidPeriodExceptionWhenPeriodIsInvalidOnSalaryDecrease() {
        BigDecimal newSalary = new BigDecimal(EMPLOYEE_NEW_SALARY);
        LocalDate decreaseDate = EMPLOYEE_HISTORY_LOCAL_DATE;

        when(employeeHistoryRepository.findByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateAsc(
                eq(employee.getId()), anyList()))
                .thenReturn(List.of());

        // ASSERT
        assertThrows(InvalidPeriodException.class, () -> {
            employeeHistoryService.createEmployeeHistorySalaryDecrease(employee, newSalary, decreaseDate);
        });
    }

    @Test
    public void shouldReturnLastEmployeeSalaryUntilDate() throws NotFoundException {
        // ARRANGE
        LocalDate targetDate = LocalDate.of(2022, 12, 31);

        when(forHistoryTypePort.findHistoryTypeByName(HistoryTypeEnum.AUMENTO_SALARIAL.getType()))
                .thenReturn(historyTypeIncrease);
        when(forHistoryTypePort.findHistoryTypeByName(HistoryTypeEnum.DISMINUCION_SALARIAL.getType()))
                .thenReturn(historyTypeDecrease);

        List<String> expectedTypes = Arrays.asList(historyTypeIncrease.getId(), historyTypeDecrease.getId());

        EmployeeHistory expectedHistory = new EmployeeHistory("1500");
        expectedHistory.setEmployee(employee);
        expectedHistory.setHistoryDate(LocalDate.of(2022, 12, 20));

        when(employeeHistoryRepository
                .findFirstByEmployee_IdAndHistoryType_IdInAndHistoryDateLessThanEqualOrderByHistoryDateDesc(
                        eq(employee.getId()), eq(expectedTypes), eq(targetDate)))
                .thenReturn(Optional.of(expectedHistory));

        // ACT
        Optional<EmployeeHistory> result = employeeHistoryService.getLastEmployeeSalaryUntilDate(employee, targetDate);

        // ASSERT
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedHistory, result.get()));
    }

    @Test
    public void shouldReturnMostRecentEmployeeSalary() throws NotFoundException {
        // ARRANGE
        when(forHistoryTypePort.findHistoryTypeByName(HistoryTypeEnum.AUMENTO_SALARIAL.getType()))
                .thenReturn(historyTypeIncrease);
        when(forHistoryTypePort.findHistoryTypeByName(HistoryTypeEnum.DISMINUCION_SALARIAL.getType()))
                .thenReturn(historyTypeDecrease);

        List<String> expectedTypes = Arrays.asList(historyTypeIncrease.getId(), historyTypeDecrease.getId());

        EmployeeHistory expectedHistory = new EmployeeHistory("1500");
        expectedHistory.setEmployee(employee);
        expectedHistory.setHistoryDate(LocalDate.of(2022, 12, 25));

        when(employeeHistoryRepository
                .findFirstByEmployee_IdAndHistoryType_IdInOrderByHistoryDateDesc(
                        eq(employee.getId()), eq(expectedTypes)))
                .thenReturn(Optional.of(expectedHistory));

        // ACT
        Optional<EmployeeHistory> result = employeeHistoryService.getMostRecentEmployeeSalary(employee);

        // ASSERT
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedHistory, result.get()));
    }

    @Test
    public void shouldCreateDeactivationHistorySuccessfully() throws NotFoundException, InvalidPeriodException {
        // ARRANGE
        when(employeeHistoryRepository.findFirstByEmployee_IdOrderByHistoryDateAsc(eq(employee.getId())))
                .thenReturn(Optional.of(employeeHistoryHiring));

        when(employeeHistoryRepository.findFirstByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateDesc(
                eq(employee.getId()), anyList()))
                .thenReturn(Optional.of(employeeHistoryLastDeactivation));

        when(forHistoryTypePort.findHistoryTypeById(deactivationReason.getId())).thenReturn(deactivationReason);

        when(employeeHistoryRepository.save(any(EmployeeHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        EmployeeHistory result = employeeHistoryService
                .createEmployeeHistoryDeactivation(employee, EMPLOYEE_DEACTIVATION_LOCAL_DATE, deactivationReason);

        // ASSERT
        ArgumentCaptor<EmployeeHistory> captor = ArgumentCaptor.forClass(EmployeeHistory.class);
        verify(employeeHistoryRepository).save(captor.capture());
        EmployeeHistory capturedHistory = captor.getValue();

        String expectedCommentary = "El empleado se ha desactivado por " + deactivationReason.getType();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedCommentary, capturedHistory.getCommentary()),
                () -> assertEquals(EMPLOYEE_DEACTIVATION_LOCAL_DATE, capturedHistory.getHistoryDate()),
                () -> assertEquals(deactivationReason, capturedHistory.getHistoryType()),
                () -> assertEquals(employee, capturedHistory.getEmployee()));
    }

    @Test
    public void shouldCreateReactivationHistorySuccessfully() throws NotFoundException, InvalidPeriodException {
        // ARRANGE
        when(employeeHistoryRepository.findFirstByEmployee_IdOrderByHistoryDateAsc(eq(employee.getId())))
                .thenReturn(Optional.of(employeeHistoryHiring)); // employeeHistoryHiring.date is 2022-11-23

        when(employeeHistoryRepository.findFirstByEmployee_IdAndHistoryType_TypeInOrderByHistoryDateDesc(
                eq(employee.getId()), anyList()))
                .thenReturn(Optional.of(employeeHistoryLastDeactivation));

        when(forHistoryTypePort.findHistoryTypeByName(HistoryTypeEnum.RECONTRATACION.getType()))
                .thenReturn(reactivationHistoryType);

        // Stub saving: simply return the passed EmployeeHistory.
        when(employeeHistoryRepository.save(any(EmployeeHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        EmployeeHistory result = employeeHistoryService.createEmployeeHistoryReactivation(employee,
                EMPLOYEE_REACTIVATION_DATE);

        // ASSERT
        ArgumentCaptor<EmployeeHistory> captor = ArgumentCaptor.forClass(EmployeeHistory.class);
        verify(employeeHistoryRepository).save(captor.capture());
        EmployeeHistory capturedHistory = captor.getValue();

        String expectedCommentary = "El empleado se ha recontratado.";

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedCommentary, capturedHistory.getCommentary()),
                () -> assertEquals(EMPLOYEE_REACTIVATION_DATE, capturedHistory.getHistoryDate()),
                () -> assertEquals(reactivationHistoryType, capturedHistory.getHistoryType()),
                () -> assertEquals(employee, capturedHistory.getEmployee()));
    }
}
