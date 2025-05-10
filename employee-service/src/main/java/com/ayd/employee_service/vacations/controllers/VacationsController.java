package com.ayd.employee_service.vacations.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.parameters.enums.ParameterEnum;
import com.ayd.employee_service.parameters.models.Parameter;
import com.ayd.employee_service.parameters.ports.ForParameterPort;
import com.ayd.shared.exceptions.*;
import com.ayd.employee_service.vacations.dtos.ChangeVacationDaysRequestDTO;
import com.ayd.employee_service.vacations.dtos.VacationDaysResponseDTO;
import com.ayd.employee_service.vacations.dtos.VacationPeriodRequestDTO;
import com.ayd.employee_service.vacations.dtos.VacationsResponseDTO;
import com.ayd.employee_service.vacations.mappers.VacationsMapper;
import com.ayd.employee_service.vacations.models.Vacations;
import com.ayd.employee_service.vacations.ports.ForVacationsPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/vacations")
@RequiredArgsConstructor
public class VacationsController {

    private final ForVacationsPort vacationsPort;
    //private final ForParameterPort parameterPort;
    private final VacationsMapper vacationsMapper;

    /*
    @Operation(summary = "Obtener los dias de vacaciones que el sistema tiene configurados",
        description = "Devuelve el valor en dias que el sistema tiene configurado de vacaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dias de vacaciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/vacation-days")
    @ResponseStatus(HttpStatus.OK)
    public VacationDaysResponseDTO getVacationDays()
            throws NotFoundException {

        Parameter vacationDays = parameterPort.findParameterByKey(ParameterEnum.DIAS_VACACIONES.getKey());
        VacationDaysResponseDTO response = new VacationDaysResponseDTO(Integer.parseInt(vacationDays.getValue()));
        return response;
    }
     */

    @Operation(summary = "Cambia los dias de vacaciones configurados",
        description = "Cambia dentro del sistema los dias de vacaciones que se tienen configurados para nuevos empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dias de vacaciones cambiados correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/vacation-days")
    @ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasAuthority('CHANGE_VACATION_DAYS')")
    public VacationDaysResponseDTO changeVacationDays(
        @RequestBody @Valid ChangeVacationDaysRequestDTO ChangeVacationDaysRequestDTO)
            throws NotFoundException {

        Integer updatedDays = vacationsPort.updateVacationDays(ChangeVacationDaysRequestDTO.getNewVacationDays());

        VacationDaysResponseDTO response = new VacationDaysResponseDTO(updatedDays);

        return response;
    }

    @Operation(summary = "Obtener todos las vacaciones de un empleado en un periodo",
        description = "Devuelve la lista de las vacaciones de un empleado en un periodo especifico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vacaciones obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{employeeId}/{periodYear}")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationsResponseDTO> getAllVacationsForEmployeeOnPeriod(
            @PathVariable("employeeId") @NotBlank(message = "El id del empleado es obligatorio") String employeeId,
            @PathVariable("periodYear") @NotBlank(message = "El periodo de las vacaciones es obligatorio") @Positive(message = "El periodo de las vacaciones debe ser un numero entero") Integer periodYear)
            throws NotFoundException {

        List<Vacations> vacations = vacationsPort.getAllVacationsForEmployeeOnPeriod(employeeId, periodYear);
        List<VacationsResponseDTO> response = vacationsMapper
                .fromVacationsListToVacationsResponseDTOs(vacations);
        return response;
    }

    @Operation(summary = "Crea las vacaciones de un empleado",
        description = "Crea las vacaciones de un empleado a partir de un periodo y varias fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacaciones creadas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{employeeId}/{periodYear}")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationsResponseDTO> createVacationsForEmployeeOnPeriod(
            @PathVariable("employeeId") @NotBlank(message = "El id del empleado es obligatorio") String employeeId,
            @PathVariable("periodYear") @NotNull(message = "El periodo de las vacaciones es obligatorio") @Positive(message = "El periodo de las vacaciones debe ser un numero entero") Integer periodYear,
            @RequestBody @Valid List<VacationPeriodRequestDTO> createVacationsRequestDTO
            )
            throws NotFoundException, InvalidPeriodException {

        List<Vacations> vacations = vacationsMapper.fromVacationPeriodRequestToVacationsList(createVacationsRequestDTO);
        List<Vacations> createdVacations = vacationsPort.createVacationsForEmployeeOnPeriod(employeeId, periodYear, vacations);
        List<VacationsResponseDTO> response = vacationsMapper
                .fromVacationsListToVacationsResponseDTOs(createdVacations);
        return response;
    }

    @Operation(summary = "Actualiza las vacaciones de un empleado",
        description = "Actualiza las vacaciones de un empleado a partir de un periodo y varias fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacaciones actualizadas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{employeeId}/{periodYear}")
    @ResponseStatus(HttpStatus.OK)
    public List<VacationsResponseDTO> updateVacationsForEmployeeOnPeriod(
            @PathVariable("employeeId") @NotBlank(message = "El id del empleado es obligatorio") String employeeId,
            @PathVariable("periodYear") @NotNull(message = "El periodo de las vacaciones es obligatorio") @Positive(message = "El periodo de las vacaciones debe ser un numero entero") Integer periodYear,
            @RequestBody @Valid List<VacationPeriodRequestDTO> createVacationsRequestDTO
            )
            throws NotFoundException, InvalidPeriodException {

        List<Vacations> vacations = vacationsMapper.fromVacationPeriodRequestToVacationsList(createVacationsRequestDTO);
        List<Vacations> createdVacations = vacationsPort.updateVacationsForEmployeeOnPeriod(employeeId, periodYear, vacations);
        List<VacationsResponseDTO> response = vacationsMapper
                .fromVacationsListToVacationsResponseDTOs(createdVacations);
        return response;
    }

    @Operation(summary = "Actualiza el estado de vacaciones de un empleado",
        description = "Actualiza el estado las vacaciones de un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacaciones actualizadas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{vacationsId}/state")
    @ResponseStatus(HttpStatus.OK)
    public VacationsResponseDTO updateVacationsState(
            @PathVariable("vacationsId") @NotBlank(message = "El id de las vacaciones es obligatorio") String vacationsId
            )
            throws NotFoundException, InvalidPeriodException {

        Vacations updatedVacations = vacationsPort.changeVacationState(vacationsId);
        VacationsResponseDTO response = vacationsMapper.fromVacationToVacationsResponseDTO(updatedVacations);

        return response;
    }

    @Operation(summary = "Obtener todos las vacaciones de un empleado en un periodo",
        description = "Devuelve la lista de las vacaciones de un empleado en un periodo especifico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vacaciones obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, List<VacationsResponseDTO>> getAllVacationsForEmployee(
            @PathVariable("employeeId") @NotBlank(message = "El id del empleado es obligatorio") String employeeId)
            throws NotFoundException {

        Map<Integer, List<Vacations>> vacations = vacationsPort.getAllVacationsForEmployee(employeeId);

        Map<Integer, List<VacationsResponseDTO>> response = vacationsMapper
                .fromVacationMapToVacationMapResponse(vacations);

        return response;
    }

}
