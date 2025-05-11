package com.ayd.employee_service.employees.services;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.employees.repositories.EmployeeTypeRepository;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.ports.ForPermissionsPort;
import com.ayd.employee_service.shared.enums.EmployeeTypeEnum;
import com.ayd.shared.exceptions.*;

import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class EmployeeTypeService implements ForEmployeeTypePort {

    private final EmployeeTypeRepository employeeTypeRepository;
    private final ForPermissionsPort forPermissionsPort;
    private final ForEmployeesPort forEmployeesPort;

    /**
     * Constructor de la clase
     *
     * @param employeeTypeRepository
     * @param forPermissionsPort
     * @param forEmployeesPort       //se inicializa como lazy porque este depende
     *                               de ForEmployeeTypePort y asi evitamos error de
     *                               recursividad
     */
    public EmployeeTypeService(EmployeeTypeRepository employeeTypeRepository, ForPermissionsPort forPermissionsPort,
            @Lazy ForEmployeesPort forEmployeesPort) {
        this.employeeTypeRepository = employeeTypeRepository;
        this.forPermissionsPort = forPermissionsPort;
        this.forEmployeesPort = forEmployeesPort;
    }

    @Override
    public EmployeeType createEmployeeType(EmployeeType employeeType, List<Permission> permissions)
            throws DuplicatedEntryException, NotFoundException {
        // si el nombre del tipo de empleado ya existe entonces lanzamos error
        if (existsEmployeeTypeByName(employeeType.getName())) {
            throw new DuplicatedEntryException("Ya existe un tipo de empleado con el mismo nombre.");
        }

        // mandamos a traer los permisos por medio de los ids especificados
        List<Permission> validPermissions = forPermissionsPort.findAllById(permissions);
        // ahora debemos eliminar los permisos que vengan repetidos
        List<Permission> uniquePermissions = validPermissions.stream()
                .distinct() // esto filtra los elementos duplicados pero se debe poner bien el equals()
                .toList();// esto vuelve a crear una lista luego de aplicar el filtro que pusimos

        // ahora los asignamos al nuevo tipo de empleado
        employeeType.setPermissions(uniquePermissions);

        // si no se lanzo excepcion entonces todos los permisos asignados al tipo de
        // empleado existen y podemos guardar
        return employeeTypeRepository.save(employeeType);
    }

    @Override
    public EmployeeType updateEmployeeType(String employeeTypeId,
            EmployeeType updatedEmployeeType,
            List<Permission> updatedPermissions)
            throws DuplicatedEntryException, NotFoundException {
        // mandamos a traer el tipo de usuario con el id
        EmployeeType existingEmployeeType = findEmployeeTypeById(employeeTypeId);

        // debemos verificar que si se esta intentando editar el por defecto que no sea en si nombre
        if (EmployeeTypeEnum.DEFAULT.getEmployeeType().getName().equals(existingEmployeeType.getName())
        && !updatedEmployeeType.getName().equals(EmployeeTypeEnum.DEFAULT.getEmployeeType().getName())) {
            throw new IllegalStateException("No se puede editar el nombre del tipo de empleado por defecto.");
        }

        // si ya existe otro usuario con el mismo nombre y no es el encontrado entonces
        // lanzar el error de info duplicada
        if (employeeTypeRepository.existsByNameAndIdIsNot(updatedEmployeeType.getName(),
                existingEmployeeType.getId())) {
            throw new DuplicatedEntryException("Ya existe un tipo de empleado con el mismo nombre.");
        }
        // se verifica que los permisos existan
        List<Permission> validPermissions = forPermissionsPort.findAllById(updatedPermissions);
        // si no lanzo exccepcion entonces podemos continuar con los set
        existingEmployeeType.setName(updatedEmployeeType.getName());
        // agregamos los permisos si han sido cambiados
        existingEmployeeType.setPermissions(validPermissions);

        return employeeTypeRepository.save(existingEmployeeType);
    }

    @Override
    public boolean deleteEmployeeTypeById(String employeeTypeId) throws NotFoundException {
        // Obtener el EmployeeType por ID
        EmployeeType existingEmployeeType = findEmployeeTypeById(employeeTypeId);

        // debemos verificar que no se esta intentando eliminar el default
        if (EmployeeTypeEnum.DEFAULT.getEmployeeType().getName().equals(existingEmployeeType.getName())) {
            throw new IllegalStateException("No se puede eliminar el tipo de empleado por defecto.");
        }

        // si no se trata del default entonces obtenemos el tipo de empleado default
        EmployeeType defaultEmployeeType = findEmployeeTypeByName(EmployeeTypeEnum.DEFAULT.getEmployeeType().getName());

        // si el typo de empleado que quiere eliminarse esta asignado aalgun empleado
        // entonces a todos los empleados que dependen debemos
        // reasignarlos al tipo empleado por defecto

        if (!existingEmployeeType.getEmployees().isEmpty()) {
            forEmployeesPort.reassignEmployeeType(existingEmployeeType.getEmployees(), defaultEmployeeType.getId());
        }

        // eliminamos el tipo de empleado
        employeeTypeRepository.delete(existingEmployeeType);

        // verificar si se eliminó correctamente
        return !employeeTypeRepository.existsById(employeeTypeId);

    }

    @Override
    public EmployeeType findEmployeeTypeByName(String employeeTypeName) throws NotFoundException {
        return employeeTypeRepository.findByName(employeeTypeName).orElseThrow(
                () -> new NotFoundException(
                        "El nombre especificado no pertenece a ningún tipo de empleado en el sistema."));
    }

    @Override
    public EmployeeType findEmployeeTypeById(String employeeTypeId) throws NotFoundException {
        return employeeTypeRepository.findById(employeeTypeId).orElseThrow(
                () -> new NotFoundException(
                        "El id especificado no pertenece a ningún tipo de empleado en el sistema."));
    }

    @Override
    public boolean existsEmployeeTypeByName(String employeeTypeName) {
        return employeeTypeRepository.existsByName(employeeTypeName);
    }

    @Override
    public boolean existsEmployeeTypeById(String employeeTypeId) {
        return employeeTypeRepository.existsById(employeeTypeId);
    }

    @Override
    public List<EmployeeType> findAllEmployeesTypes() {
        return employeeTypeRepository.findAll();
    }

}
