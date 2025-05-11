package com.ayd.employee_service.employees.ports;

import java.util.List;

import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.shared.exceptions.*;

public interface ForEmployeeTypePort {

        /**
         * Crea un nuevo tipo de empleado en el sistema.
         *
         * @param employeeType el tipo de empleado a crear
         * @param permissions  permisos a ser asignados al nuevo tipo de
         *                     empleado
         * @return el tipo de empleado creado
         * @throws DuplicatedEntryException si el tipo de empleado ya existe
         * @throws NotFoundException        si los permisos especificados no existen
         */
        public EmployeeType createEmployeeType(EmployeeType employeeType,
                        List<Permission> permissions)
                        throws DuplicatedEntryException, NotFoundException;

        /**
         * Actualiza un tipo de empleado existente en el sistema,
         * en caso en los permisos a asignar hace falta uno ya asignado
         * se borrara la asignacion, en caso en los permisos a asignar hay un
         * permiso nuevo permiso a asignar entonces se haya lal asignacion.
         *
         * @param employeeTypeId  id del tipo de empleado a actualizar
         * @param newEmployeeType los datos actualizados del tipo de empleado
         * @param permissions     permisos a asignar
         * @return el tipo de empleado actualizado
         * @throws DuplicatedEntryException si el nombre del tipo de empleado ya existe
         * @throws NotFoundException        si el tipo de empleado no existe
         */
        public EmployeeType updateEmployeeType(String employeeTypeId,
                        EmployeeType updatedEmployeeType,
                        List<Permission> updatedPermissions)
                        throws DuplicatedEntryException, NotFoundException;

        /**
         * Elimina un tipo de empleado por su ID.
         *
         * @param employeeTypeId el ID del tipo de empleado a eliminar
         * @return true si la eliminación fue exitosa
         * @throws NotFoundException si el tipo de empleado no existe
         */
        public boolean deleteEmployeeTypeById(String employeeTypeId)
                        throws NotFoundException;

        /**
         * Verifica si existe un tipo de empleado con el nombre especificado.
         *
         * @param employeeTypeName el nombre del tipo de empleado
         * @return true si existe, false en caso contrario
         */
        public boolean existsEmployeeTypeByName(String employeeTypeName);

        /**
         * Verifica si existe un tipo de empleado con el ID especificado.
         *
         * @param employeeTypeId el ID del tipo de empleado
         * @return true si existe, false en caso contrario
         */
        public boolean existsEmployeeTypeById(String employeeTypeId);

        /**
         * Obtiene todos los tipos de empleados existentes en el sistema.
         *
         * @return lista de tipos de empleados
         */
        public List<EmployeeType> findAllEmployeesTypes();

        /**
         * Busca un tipo de empleado por su nombre.
         *
         * @param employeeType el tipo de empleado con el nombre a buscar
         * @return el tipo de empleado encontrado
         * @throws NotFoundException si el tipo de empleado no existe
         */
        public EmployeeType findEmployeeTypeByName(String employeeTypeName) throws NotFoundException;

        /**
         * Busca un tipo de empleado por su ID.
         *
         * @param employeeType el tipo de empleado con el ID a buscar
         * @return el tipo de empleado encontrado
         * @throws NotFoundException si el tipo de empleado no existe
         */
        EmployeeType findEmployeeTypeById(String employeeTypeId) throws NotFoundException;

}
