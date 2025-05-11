package com.ayd.reservation_service.schedule.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.reservation_service.schedule.dtos.CreateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.dtos.ScheduleResponseDTO;
import com.ayd.reservation_service.schedule.dtos.UpdateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.mappers.ScheduleMapper;
import com.ayd.reservation_service.schedule.models.Schedule;
import com.ayd.reservation_service.schedule.ports.ForSchedulePort;
import com.ayd.shared.exceptions.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ForSchedulePort forSchedulePort;
    private final ScheduleMapper scheduleMapper;

    @Operation(summary = "Obtener horario por ID", description = "Devuelve la información de un horario específico utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponseDTO getSchedule(@PathVariable String id) throws NotFoundException {
        Schedule schedule = forSchedulePort.getScheduleById(id);
        return scheduleMapper.formScheduleToScheduleResponseDTO(schedule);
    }

    @Operation(summary = "Obtener horarios por modalidad", description = "Devuelve una lista de horarios filtrados por modalidad en línea (true para online, false para presencial).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de horarios obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/online/{online}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponseDTO> getSchedulesByOnline(@PathVariable boolean online) {
        List<Schedule> schedules = forSchedulePort.getSchedulesByOnline(online);
        return scheduleMapper.formScheduleListToScheduleResponseDTOList(schedules);
    }

    @Operation(summary = "Crear un nuevo horario", description = "Registra un nuevo horario en el sistema. Valida que no exista un duplicado y que los datos sean consistentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "409", description = "Conflicto: horario duplicado o estado inválido"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('CREATE_SCHEDULE')")
    public ScheduleResponseDTO createSchedule(@Valid @RequestBody CreateScheduleRequestDTO createScheduleRequest)
            throws DuplicatedEntryException, IllegalStateException {
        Schedule schedule = forSchedulePort.createSchedule(createScheduleRequest);
        return scheduleMapper.formScheduleToScheduleResponseDTO(schedule);
    }

    @Operation(summary = "Actualizar horario existente", description = "Actualiza los datos de un horario identificado por su ID. Valida que no haya duplicados y que los datos sean consistentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto: horario duplicado o estado inválido"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('EDIT_SCHEDULE')")
    public ScheduleResponseDTO updateSchedule(@PathVariable String id,
            @Valid @RequestBody UpdateScheduleRequestDTO updateScheduleRequest)
            throws NotFoundException, DuplicatedEntryException, IllegalStateException {
        Schedule schedule = forSchedulePort.updateSchedule(id, updateScheduleRequest);
        return scheduleMapper.formScheduleToScheduleResponseDTO(schedule);
    }

    @Operation(summary = "Eliminar un horario", description = "Elimina un horario del sistema utilizando su identificador único. Si no se encuentra, lanza un error.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente (sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('DELETE_SCHEDULE')")
    public void deleteSchedule(@PathVariable String id) throws NotFoundException {
        forSchedulePort.deleteSchedule(id);
    }

}
