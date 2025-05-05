package com.ayd.employee_service.employees.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForEmployeeHistoryPort;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.employees.repositories.EmployeeRepository;
import com.ayd.employee_service.employees.specifications.EmployeeSpecifications;
import com.ayd.employee_service.shared.enums.EmployeeTypeEnum;
import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.InvalidPeriodException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;
import com.ayd.employee_service.vacations.ports.ForVacationsPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class EmployeeService implements ForEmployeesPort {

    private final EmployeeRepository employeeRepository;
    private final ForEmployeeTypePort forEmployeeTypePort;
    private final ForEmployeeHistoryPort forEmployeeHistoryPort;
    private final ForUsersPort userService;

    private final @Lazy ForVacationsPort vacationsPort;

    @Override
    public Employee createEmployee(Employee newEmployee, EmployeeType employeeType, User newUser,
            EmployeeHistory employeeHistoryDate)
            throws DuplicatedEntryException, NotFoundException {
        // veficar que el tipo de empleado si exista
        EmployeeType existingEmployeeType = forEmployeeTypePort.findEmployeeTypeById(employeeType.getId());
        // mandar a guardar el usuario
        User user = userService.createUser(newUser);

        // crea el primer registro del empleado en el historial (su contratacion)
        EmployeeHistory createdEmployeeHistory = forEmployeeHistoryPort.createEmployeeHistoryHiring(newEmployee,
                employeeHistoryDate.getHistoryDate());

        // guardar el empledo
        newEmployee.setUser(user);
        newEmployee.setEmployeeType(existingEmployeeType);
        ArrayList<EmployeeHistory> employeeHistories = new ArrayList<>();
        employeeHistories.add(createdEmployeeHistory);
        newEmployee.setEmployeeHistories(employeeHistories);
        user.setEmployee(newEmployee);

        Employee createdEmployee = employeeRepository.save(newEmployee);

        vacationsPort.createRandomVacationsForEmployee(createdEmployee.getId());

        // guardar el historial del empleado inicial
        return createdEmployee;
    }

    @Override
    public Employee updateEmployee(String currentId, Employee newData, EmployeeType employeeType)
            throws NotFoundException {
        // traer el empleado por id
        Employee currentEmployee = findEmployeeById(currentId);

        // traer el tipo de empleado que se desea asignar
        EmployeeType existingEmployeeType = forEmployeeTypePort.findEmployeeTypeById(employeeType.getId());

        // editar el empleado existente con la información de newData
        currentEmployee.setCui(newData.getCui());
        currentEmployee.setFirstName(newData.getFirstName());
        currentEmployee.setLastName(newData.getLastName());
        currentEmployee.setSalary(newData.getSalary());
        currentEmployee.setIgssPercentage(newData.getIgssPercentage());
        currentEmployee.setIrtraPercentage(newData.getIrtraPercentage());
        currentEmployee.setEmployeeType(existingEmployeeType);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public Employee updateEmployeeSalary(String currentId, BigDecimal newSalary, LocalDate salaryDate)
            throws NotFoundException, InvalidPeriodException {
        Employee currentEmployee = findEmployeeById(currentId);

        List<EmployeeHistory> employeeHistories = currentEmployee.getEmployeeHistories();

        BigDecimal comparisonSalary = currentEmployee.getSalary();

        // se verifica si el salario aumento o disminuyo (para la fecha indicada) y se
        // registra en el historial del empleado
        Optional<EmployeeHistory> currentSalaryUntilDateHistory = forEmployeeHistoryPort
                .getLastEmployeeSalaryUntilDate(currentEmployee, salaryDate);

        // si ya se modifico el salario anteriormente (a la fecha) se usa ese registro
        if (currentSalaryUntilDateHistory.isPresent()) {
            comparisonSalary = new BigDecimal(currentSalaryUntilDateHistory.get().getCommentary());
        } else {
            // si no hay se utiliza el primer salario que se le dio, el de contratacion
            EmployeeHistory hiring = currentEmployee.getEmployeeHistories().get(0);
            String hiringCommentary = hiring.getCommentary();
            BigDecimal hiringSalary = new BigDecimal(
                    hiringCommentary.substring(hiringCommentary.indexOf("Q.") + 2).trim());
            comparisonSalary = hiringSalary;
        }

        if (comparisonSalary.compareTo(newSalary) == -1) {
            // aumento
            EmployeeHistory createdEmployeeHistory = forEmployeeHistoryPort
                    .createEmployeeHistorySalaryIncrease(currentEmployee, newSalary, salaryDate);
            employeeHistories.add(createdEmployeeHistory);
        } else if (comparisonSalary.compareTo(newSalary) == 1) {
            // disminuyo
            EmployeeHistory createdEmployeeHistory = forEmployeeHistoryPort
                    .createEmployeeHistorySalaryDecrease(currentEmployee, newSalary, salaryDate);
            employeeHistories.add(createdEmployeeHistory);
        }

        // guardar el historial del empleado
        currentEmployee.setEmployeeHistories(employeeHistories);

        // se usa el salario mas reciente segun el historial para asignarle al empleado
        Optional<EmployeeHistory> mostRecentEmployeeSalaryOptional = this.forEmployeeHistoryPort
                .getMostRecentEmployeeSalary(currentEmployee);
        if (mostRecentEmployeeSalaryOptional.isEmpty()) {
            // si esta vacio se usa el que se acaba de ingresar
            currentEmployee.setSalary(newSalary);
        } else {
            // si no esta vacio se usa el ultimo obtenido
            EmployeeHistory employeeHistory = mostRecentEmployeeSalaryOptional.get();
            currentEmployee.setSalary(new BigDecimal(employeeHistory.getCommentary()));
        }

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public Employee desactivateEmployee(String currentId, LocalDate deactivationDate, HistoryType historyTypeReason)
            throws NotFoundException, IllegalStateException, InvalidPeriodException {
        // traer el empleado por id
        Employee currentEmployee = findEmployeeById(currentId);

        // si ya esta desactivado entonces lanzamos error
        if (currentEmployee.getDesactivatedAt() != null) {
            // indicamos que se llamo el metodo en un momento inapropiado
            throw new IllegalStateException("El empleado ya está desactivado.");
        }

        EmployeeHistory createdEmployeeHistory = forEmployeeHistoryPort
                .createEmployeeHistoryDeactivation(currentEmployee, deactivationDate, historyTypeReason);

        // le cambiamos el estado a su usuario y al empleado
        LocalDate desactivatedDate = deactivationDate;
        currentEmployee.setDesactivatedAt(desactivatedDate);
        currentEmployee.getUser().setDesactivatedAt(desactivatedDate);

        // se agrega la desactivacion al historial del empleado
        List<EmployeeHistory> employeeHistories = currentEmployee.getEmployeeHistories();
        employeeHistories.add(createdEmployeeHistory);
        currentEmployee.setEmployeeHistories(employeeHistories);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public Employee reactivateEmployee(String currentId, LocalDate reactivationDate)
            throws NotFoundException, IllegalStateException, InvalidPeriodException {
        // traer el empleado por id
        Employee currentEmployee = findEmployeeById(currentId);

        // si ya esta desactivado entonces lanzamos error
        if (currentEmployee.getDesactivatedAt() == null) {
            // indicamos que se llamo el metodo en un momento inapropiado
            throw new IllegalStateException("El empleado esta activado.");
        }

        EmployeeHistory reactivatedEmployeeHistory = forEmployeeHistoryPort
                .createEmployeeHistoryReactivation(currentEmployee, reactivationDate);

        // le cambiamos el estado a su usuario y al empleado
        currentEmployee.setDesactivatedAt(null);
        currentEmployee.getUser().setDesactivatedAt(null);

        // se agrega la desactivacion al historial del empleado
        List<EmployeeHistory> employeeHistories = currentEmployee.getEmployeeHistories();
        employeeHistories.add(reactivatedEmployeeHistory);
        currentEmployee.setEmployeeHistories(employeeHistories);

        return employeeRepository.save(currentEmployee);
    }

    @Override
    public Employee reassignEmployeeType(String employeeId, String employeeTypeId) throws NotFoundException {
        Employee exisitingEmployee = findEmployeeById(employeeId);
        EmployeeType existinEmployeeType = forEmployeeTypePort.findEmployeeTypeById(employeeTypeId);
        exisitingEmployee.setEmployeeType(existinEmployeeType);
        return exisitingEmployee;
    }

    @Override
    public List<Employee> reassignEmployeeType(List<Employee> employeeIds, String employeeTypeId)
            throws NotFoundException {
        List<Employee> updatedEmployees = new ArrayList<>();
        for (Employee employeeId : employeeIds) {
            Employee reassignEmployeeType = reassignEmployeeType(employeeId.getId(), employeeTypeId);
            updatedEmployees.add(reassignEmployeeType);
        }
        return updatedEmployees;
    }

    @Override
    public Employee findEmployeeById(String employeeId) throws NotFoundException {
        String errorMessage = String.format("El id %s no pertenece a ningun empleado.", employeeId);
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NotFoundException(errorMessage));

        return employee;
    }

    @Override
    public Employee findEmployeeByUsername(String username) throws NotFoundException {
        String errorMessage = String.format("El nombre de usuario %s no pertenece a ningun empleado.", username);
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        Employee employee = employeeRepository.findByUser_Username(username).orElseThrow(
                () -> new NotFoundException(errorMessage));

        return employee;
    }

    @Override
    public List<Employee> findEmployees() {
        // manda a traer el employee si el optional esta vacio entonces retorna un
        // notfound exception
        List<Employee> employees = employeeRepository.findAll();

        return employees;
    }

    @Override
    public List<Employee> findEmployeesInvoiceForPeriod(Integer periodYear) {
        List<Employee> employees = employeeRepository.findEmployeesWithAllVacationsUsed(periodYear);

        return employees;
    }

    @Override
    public List<Employee> getEmployeesByType(String employeeTypeId, String search) throws NotFoundException {
        // Verificamos si existe el tipo de empleado
        EmployeeType employeeType = forEmployeeTypePort.findEmployeeTypeById(employeeTypeId);
        Specification<Employee> spec = Specification
                .where(EmployeeSpecifications.hasEmployeeTypeId(employeeType.getId()))
                .and(EmployeeSpecifications.hasFirstName(search))
                .and(EmployeeSpecifications.hasLastName(search))
                .and(EmployeeSpecifications.isActive(true));
        List<Employee> employees = employeeRepository.findAll(spec);
        return employees;
    }

    @Override
    public List<Employee> getDoctors(String search) throws NotFoundException {
        // Traemos los empleados por el tipo de empleado
        EmployeeType employeeType = forEmployeeTypePort.findEmployeeTypeByName(EmployeeTypeEnum.DOCTOR.name());
        List<Employee> employees = getEmployeesByType(employeeType.getId(), search);
        return employees;
    }

    @Override
    public List<Employee> getNurses(String search) throws NotFoundException {
        // Traemos los empleados por el tipo de empleado
        EmployeeType employeeType = forEmployeeTypePort.findEmployeeTypeByName(EmployeeTypeEnum.ENFERMERO.name());
        List<Employee> employees = getEmployeesByType(employeeType.getId(), search);
        return employees;
    }
}
