package com.ayd.employee_service.employees.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.employees.dtos.CompoundEmployeeResponseDTO;
import com.ayd.employee_service.employees.dtos.CreateEmployeeRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeDeactivateRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeHistoryResponseDTO;
import com.ayd.employee_service.employees.dtos.EmployeeReactivateRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeResponseDTO;
import com.ayd.employee_service.employees.dtos.EmployeeSalaryRequestDTO;
import com.ayd.employee_service.employees.mappers.EmployeeHistoryMapper;
import com.ayd.employee_service.employees.mappers.EmployeeMapper;
import com.ayd.employee_service.employees.mappers.EmployeeTypeMapper;
import com.ayd.employee_service.employees.mappers.HistoryTypeMapper;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForEmployeeHistoryPort;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.InvalidPeriodException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.mappers.UserMapper;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.vacations.dtos.VacationsResponseDTO;
import com.ayd.employee_service.vacations.mappers.VacationsMapper;
import com.ayd.employee_service.vacations.models.Vacations;
import com.ayd.employee_service.vacations.ports.ForVacationsPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")

@RequiredArgsConstructor
public class EmployeesController {

        private final ForEmployeesPort employeesPort;
        private final ForEmployeeHistoryPort employeeHistoryPort;
        private final ForVacationsPort vacationsPort;

        private final EmployeeTypeMapper employeeTypeMapper;
        private final EmployeeMapper employeeMapper;
        private final VacationsMapper vacationsMapper;
        private final UserMapper userMapper;
        private final HistoryTypeMapper historyTypeMapper;
        private final EmployeeHistoryMapper employeeHistoryMapper;

        @Operation(summary = "Crear un nuevo empleado", description = "Este endpoint permite la creación de un nuevo empleado en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "409", description = "Conflicto - Username duplicado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "No encontrado - Tipo de empleado no existe", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping
        @PreAuthorize("hasAuthority('CREATE_EMPLOYEE')")
        public ResponseEntity<EmployeeResponseDTO> createEmployee(
                        @RequestBody @Valid CreateEmployeeRequestDTO request)
                        throws DuplicatedEntryException, NotFoundException {
                // extraer los parametros para la creacion del employee
                Employee newEmployee = employeeMapper.fromCreateEmployeeRequestDtoToEmployee(request);
                EmployeeType employeeType = employeeTypeMapper
                                .fromIdRequestDtoToEmployeeType(request.getEmployeeTypeId());
                User newUser = userMapper.fromCreateUserRequestDtoToUser(request.getCreateUserRequestDTO());
                EmployeeHistory employeeHistoryDate = employeeHistoryMapper
                                .fromEmployeeHistoryDateRequestDtoToEmployeeHistory(
                                                request.getEmployeeHistoryDateRequestDTO());

                // mandar a crear el employee al port
                Employee result = employeesPort.createEmployee(newEmployee, employeeType, newUser, employeeHistoryDate);

                // convertir el Employee al dto
                EmployeeResponseDTO response = employeeMapper.fromEmployeeToResponse(result);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Edita un empleado", description = "Este endpoint permite la edición de un empleado en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleado editado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Recursos no econtrados, el usuario a editar no existe o el tipo de empleado no existe.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")

        })
        @PreAuthorize("hasAuthority('EDIT_EMPLOYEE')")
        @PatchMapping("/{employeeId}")
        public ResponseEntity<EmployeeResponseDTO> updateEmployee(
                        @PathVariable("employeeId") String employeeId,
                        @RequestBody @Valid EmployeeRequestDTO request)
                        throws NotFoundException {

                // extraer los parametros para la creacion del employee
                Employee newEmployee = employeeMapper.fromEmployeeRequestDtoToEmployee(request);
                EmployeeType employeeType = employeeTypeMapper
                                .fromIdRequestDtoToEmployeeType(request.getEmployeeTypeId());

                // mandar a editar el employee al port
                Employee result = employeesPort.updateEmployee(employeeId, newEmployee, employeeType);

                // convertir el Employee al dto
                EmployeeResponseDTO response = employeeMapper.fromEmployeeToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Edita el salario de un empleado", description = "Este endpoint permite la edición del salario de un empleado en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleado editado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Recursos no econtrados, el usuario a editar no existe o el tipo de empleado no existe.", content = @Content(mediaType = "application/json")),

        })
        @PatchMapping("/{employeeId}/salary")
        @PreAuthorize("hasAuthority('UPDATE_EMPLOYEE_SALARY')")
        public ResponseEntity<EmployeeResponseDTO> updateEmployeeSalary(
                        @RequestBody @Valid EmployeeSalaryRequestDTO request,
                        @PathVariable("employeeId") @NotBlank(message = "El id del empleado no puede estar vacio") String employeeId)
                        throws NotFoundException, InvalidPeriodException {

                // mandar a editar el employee al port
                Employee result = employeesPort.updateEmployeeSalary(employeeId, request.getSalary(),
                                request.getSalaryDate());

                // convertir el Employee al dto
                EmployeeResponseDTO response = employeeMapper.fromEmployeeToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Desactiva un empleado", description = "Este endpoint permite la cambiar el estado de desactivatedAt de un empleado en el sistema segun su id.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Empleado desactivado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Recursos no econtrados, el usuario a desactivar.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "409", description = "Conflicto, el empleaod ya esta desactivado.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/{employeeId}/desactivate")
        @PreAuthorize("hasAuthority('DESACTIVATE_EMPLOYEE')")
        public ResponseEntity<Void> desactivateEmployee(
                @RequestBody @Valid EmployeeDeactivateRequestDTO request,
                @PathVariable("employeeId") String employeeId)
                        throws NotFoundException, IllegalStateException, InvalidPeriodException {

                HistoryType historyTypeReason = historyTypeMapper.fromIdRequestDtoToHistoryType(request.getHistoryTypeId());
                // mandar a desactivar el employee al port
                employeesPort.desactivateEmployee(employeeId, request.getDeactivationDate(), historyTypeReason);

                return ResponseEntity.noContent().build();

        }

        @Operation(summary = "Reactiva un empleado", description = "Este endpoint permite la reactivar el estado de desactivatedAt de un empleado en el sistema segun su id.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Empleado desactivado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Recursos no econtrados, el usuario a desactivar.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "409", description = "Conflicto, el empleaod ya esta desactivado.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PreAuthorize("hasAuthority('RESACTIVATE_EMPLOYEE')")
        @PatchMapping("/{employeeId}/reactivate")
        public ResponseEntity<Void> reactivateEmployee(
                @RequestBody @Valid EmployeeReactivateRequestDTO request,
                @PathVariable("employeeId") String employeeId)
                        throws NotFoundException, IllegalStateException, InvalidPeriodException {

                // mandar a desactivar el employee al port
                employeesPort.reactivateEmployee(employeeId, request.getReactivationDate());

                return ResponseEntity.noContent().build();

        }

        @Operation(summary = "Busca un empleado", description = "Este endpoint permite la busqueda de un empleado en base a su Id.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{employeeId}")
        public ResponseEntity<CompoundEmployeeResponseDTO> findEmployeeById(
                        @PathVariable("employeeId") String employeeId)
                        throws NotFoundException {

                // mandar a obtener el employee al port
                Employee result = employeesPort.findEmployeeById(employeeId);

                List<EmployeeHistory> historyEmployee = employeeHistoryPort.getEmployeeHistory(result);
                List<EmployeeHistoryResponseDTO> employeeHistories = employeeHistoryMapper
                                .fromEmployeeHistoriesToEmployeeHistoryDtoList(historyEmployee);

                // se obtienen las vacaciones del empleado
                Map<Integer, List<Vacations>> vacations = vacationsPort.getAllVacationsForEmployee(employeeId);
                Map<Integer, List<VacationsResponseDTO>> response = vacationsMapper
                            .fromVacationMapToVacationMapResponse(vacations);

                // convertir el Employee al dto
                EmployeeResponseDTO employeeResponseDTO = employeeMapper.fromEmployeeToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(
                                new CompoundEmployeeResponseDTO(employeeResponseDTO, result.getUser().getUsername(),
                                                employeeHistories, response));
        }

        @Operation(summary = "Obtener todos los empleados", description = "Este endpoint permite la busqueda de todos los empleados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/")
        public ResponseEntity<List<EmployeeResponseDTO>> findEmployees() {
                // mandar a crear el employee al port
                List<Employee> result = employeesPort.findEmployees();

                // convertir el Employee al dto
                List<EmployeeResponseDTO> response = employeeMapper.fromEmployeesToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Obtener todos los empleados para generar finiquito por periodo",
            description = "Se obtienen todos los empleados a los que se puede generar su finiquito en un periodo dado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{periodYear}/vacationsInvoice")
        @PreAuthorize("hasAuthority('GET_ALL_INVOICES')")
        public ResponseEntity<List<EmployeeResponseDTO>> findVacationInvoiceEmployeesForPeriod(
                        @PathVariable("periodYear") Integer periodYear)
        {
                // mandar a crear el employee al port
                List<Employee> result = employeesPort.findEmployeesInvoiceForPeriod(periodYear);

                // convertir el Employee al dto
                List<EmployeeResponseDTO> response = employeeMapper.fromEmployeesToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Obtener todos los empleados de tipo doctor", description = "Este endpoint permite la busqueda de todos los empleados de tipo doctor.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "No encontrado - Tipo de empleado no existe", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/doctors")
        public ResponseEntity<List<EmployeeResponseDTO>> findDoctorEmployees(
                        @RequestParam(value = "search", required = false) String search) throws NotFoundException {
                // mandar a crear el employee al port
                List<Employee> result = employeesPort.getDoctors(search);

                // convertir el Employee al dto
                List<EmployeeResponseDTO> response = employeeMapper.fromEmployeesToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Obtener todos los empleados de tipo enfermera", description = "Este endpoint permite la busqueda de todos los empleados de tipo enfermera.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Empleados encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "No encontrado - Tipo de empleado no existe", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/nurses")
        public ResponseEntity<List<EmployeeResponseDTO>> findNurseEmployees(
                        @RequestParam(value = "search", required = false) String search) throws NotFoundException {
                // mandar a crear el employee al port
                List<Employee> result = employeesPort.getNurses(search);

                // convertir el Employee al dto
                List<EmployeeResponseDTO> response = employeeMapper.fromEmployeesToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }
}
