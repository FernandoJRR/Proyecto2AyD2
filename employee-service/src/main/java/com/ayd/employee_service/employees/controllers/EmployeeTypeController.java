package com.ayd.employee_service.employees.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.employees.dtos.EmployeeTypeResponseDTO;
import com.ayd.employee_service.employees.dtos.SaveEmployeeTypeRequestDTO;
import com.ayd.employee_service.employees.mappers.EmployeeTypeMapper;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.permissions.mappers.PermissionMapper;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee-types")

@RequiredArgsConstructor
public class EmployeeTypeController {

        private final ForEmployeeTypePort employeeTypePort;
        private final EmployeeTypeMapper employeeTypeMapper;
        private final PermissionMapper permissionMapper;

        @Operation(summary = "Obtener todos los tipos de empleados", description = "Devuelve la lista de los typos de empleados existentes.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de tipos de empleados obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<EmployeeTypeResponseDTO> getEmployeesTypes() {

                // mandar a crear el employee al port
                List<EmployeeType> result = employeeTypePort.findAllEmployeesTypes();

                // convertir el Employee al dto
                List<EmployeeTypeResponseDTO> response = employeeTypeMapper
                                .fromEmployeeTypeListToEmployeeTypeResponseDtoList(result);

                return response;
        }

        @Operation(summary = "Obtiene un tipo de empleado por su ID", description = "Este endpoint permite obtener la información de un tipo de empleado específico utilizando su ID. Si el tipo de empleado no existe, se lanza una excepción.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tipo de empleado encontrado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - No existe un tipo de empleado con el ID especificado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{employeeTypeId}")
        @ResponseStatus(HttpStatus.OK)
        public EmployeeTypeResponseDTO finEmployeeTypeById(
                        @PathVariable("employeeTypeId") String employeeTypeId) throws NotFoundException {

                // mandar a busar por el id
                EmployeeType result = employeeTypePort.findEmployeeTypeById(employeeTypeId);

                // convertir el employee type al dto
                EmployeeTypeResponseDTO response = employeeTypeMapper.fromEmployeeTypeToEmployeeTypeResponseDto(result);
                return response;
        }

        @Operation(summary = "Crea un nuevo tipo de empleado en el sistema", description = "Este endpoint permite la creación de un nuevo tipo de empleado, asegurando que el nombre no esté duplicado y que los permisos asignados existan en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Tipo de empleado creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida - Validaciones fallidas"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - Si algun permiso no se encuentra por medio de los ids especificados"),
                        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tipo de empleado con el mismo nombre"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @PreAuthorize("hasAuthority('CREATE_EMPLOYEE_TYPE')")
        public EmployeeTypeResponseDTO createTypeEmployee(
                        @RequestBody @Valid SaveEmployeeTypeRequestDTO request)
                        throws DuplicatedEntryException, NotFoundException {
                // mapeamos la request
                EmployeeType employeeTypeToCreate = employeeTypeMapper.fromCreateEmployeeTypeDtoToEmployeeType(request);
                // creamos los permisos unicamente con sus ids inicializados
                List<Permission> assignedPermissions = permissionMapper
                                .fromIdsRequestDtoToPermissions(request.getPermissions());
                // mandar a crear el employee l port
                EmployeeType savedEmployeeType = employeeTypePort.createEmployeeType(employeeTypeToCreate,
                                assignedPermissions);
                // convertir el EmployeeTyoe al dto
                EmployeeTypeResponseDTO response = employeeTypeMapper
                                .fromEmployeeTypeToEmployeeTypeResponseDto(savedEmployeeType);

                return response;
        }

        @Operation(summary = "Edita un tipo de empleado existente", description = "Este endpoint permite editar un tipo de empleado ya existente utilizando su ID. Se valida que el nuevo nombre no esté duplicado y que los permisos proporcionados existan en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Tipo de empleado actualizado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida - Validaciones fallidas"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - No existe un tipo de empleado o permiso con los IDs especificados"),
                        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un tipo de empleado con el mismo nombre"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/{employeeTypeId}")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasAuthority('CREATE_EMPLOYEE_TYPE')")
        public EmployeeTypeResponseDTO editEmployeeType(
                        @RequestBody @Valid SaveEmployeeTypeRequestDTO request,
                        @PathVariable("employeeTypeId") String employeeTypeId)
                        throws DuplicatedEntryException, NotFoundException {
                // mapeamos la request
                EmployeeType employeeTypeToUpdate = employeeTypeMapper.fromCreateEmployeeTypeDtoToEmployeeType(request);
                // creamos los permisos unicamente con sus ids inicializados
                List<Permission> assignedPermissions = permissionMapper
                                .fromIdsRequestDtoToPermissions(request.getPermissions());
                // mandar a crear el employee l port
                EmployeeType savedEmployeeType = employeeTypePort.updateEmployeeType(
                                employeeTypeId, employeeTypeToUpdate,
                                assignedPermissions);

                // convertir el EmployeeTyoe al dto
                EmployeeTypeResponseDTO response = employeeTypeMapper
                                .fromEmployeeTypeToEmployeeTypeResponseDto(savedEmployeeType);

                return response;
        }

        @Operation(summary = "Elimina un tipo de empleado por su ID", description = "Este endpoint permite eliminar un tipo de empleado del sistema. Si el tipo está asignado a empleados, estos serán reasignados al tipo de empleado por defecto antes de eliminar. No se permite eliminar el tipo de empleado por defecto.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Tipo de empleado eliminado exitosamente"),
                        @ApiResponse(responseCode = "409", description = "Conflicto - No se puede eliminar el tipo de empleado por defecto"),
                        @ApiResponse(responseCode = "404", description = "Recurso no encontrado - No existe un tipo de empleado con el ID especificado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @DeleteMapping("/{employeeTypeId}")
        @PreAuthorize("hasAuthority('DELETE_EMPLOYEE_TYPE')")
        public ResponseEntity<EmployeeTypeResponseDTO> deleteEmployeeTypeById(
                        @PathVariable("employeeTypeId") String employeeTypeId)
                        throws NotFoundException {

                // mandar a eliminar el tipo de empleado
                employeeTypePort.deleteEmployeeTypeById(
                                employeeTypeId);

                return ResponseEntity.noContent().build();
        }
}
