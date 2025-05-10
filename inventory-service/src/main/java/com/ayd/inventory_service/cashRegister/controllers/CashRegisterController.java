package com.ayd.inventory_service.cashRegister.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.inventory_service.cashRegister.dtos.CashRegisterResponseDTO;
import com.ayd.inventory_service.cashRegister.dtos.CreateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.SpecificationCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.UpdateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.mappers.CashRegisterMapper;
import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.inventory_service.cashRegister.ports.ForCashRegisterPort;
import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cash-registers")
@RequiredArgsConstructor
public class CashRegisterController {
    private final ForCashRegisterPort forCashRegisterPort;
    private final CashRegisterMapper cashRegisterMapper;

    @Operation(summary = "Obtener todas las cajas registradoras", description = "Devuelve una lista de cajas registradoras. Se pueden aplicar filtros opcionales a través del cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cajas registradoras obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CashRegisterResponseDTO> findAll(
            @RequestBody(required = false) SpecificationCashRegisterRequestDTO specificationCashRegisterRequestDTO) {
        List<CashRegister> cashRegisters = forCashRegisterPort
                .findAllBySpecification(specificationCashRegisterRequestDTO);
        return cashRegisterMapper.fromCashRegisterListToCashRegisterResponseDTOList(cashRegisters);
    }

    @Operation(summary = "Obtener caja registradora por ID", description = "Devuelve la información de una caja registradora a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caja registradora encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Caja registradora no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CashRegisterResponseDTO findById(@PathVariable String id) throws NotFoundException {
        CashRegister cashRegister = forCashRegisterPort.findById(id);
        return cashRegisterMapper.fromCashRegisterToCashRegisterResponseDTO(cashRegister);
    }

    @Operation(summary = "Crear una nueva caja registradora", description = "Registra una nueva caja registradora en el sistema. Valida duplicados y entidades relacionadas requeridas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Caja registradora creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Entidad relacionada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una caja registradora con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CashRegisterResponseDTO create(@RequestBody @Valid CreateCashRegisterRequestDTO createCashRegisterRequestDTO)
            throws NotFoundException, DuplicatedEntryException {
        CashRegister cashRegister = forCashRegisterPort.save(createCashRegisterRequestDTO);
        return cashRegisterMapper.fromCashRegisterToCashRegisterResponseDTO(cashRegister);
    }

    @Operation(summary = "Actualizar una caja registradora", description = "Actualiza completamente los datos de una caja registradora existente identificada por su ID. Valida duplicados y existencia previa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caja registradora actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Caja registradora no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe otra caja registradora con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CashRegisterResponseDTO update(@PathVariable String id,
            @RequestBody @Valid UpdateCashRegisterRequestDTO updateCashRegisterRequestDTO)
            throws NotFoundException, DuplicatedEntryException {
        CashRegister cashRegister = forCashRegisterPort.update(id, updateCashRegisterRequestDTO);
        return cashRegisterMapper.fromCashRegisterToCashRegisterResponseDTO(cashRegister);
    }

    @Operation(summary = "Alternar estado activo de una caja registradora", description = "Activa o desactiva una caja registradora según su estado actual. Requiere el ID de la caja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la caja registradora actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Caja registradora no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PatchMapping("/{id}/toogle")
    @ResponseStatus(HttpStatus.OK)
    public CashRegisterResponseDTO toggle(@PathVariable String id) throws NotFoundException {
        CashRegister cashRegister = forCashRegisterPort.toggleActive(id);
        return cashRegisterMapper.fromCashRegisterToCashRegisterResponseDTO(cashRegister);
    }

    @Operation(summary = "Obtener caja registradora por ID de empleado", description = "Devuelve la caja registradora asignada a un empleado específico, identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caja registradora encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado o caja registradora no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public CashRegisterResponseDTO findByEmployeeId(@PathVariable String employeeId) throws NotFoundException {
        CashRegister cashRegister = forCashRegisterPort.findByEmployeeId(employeeId);
        return cashRegisterMapper.fromCashRegisterToCashRegisterResponseDTO(cashRegister);
    }

}
