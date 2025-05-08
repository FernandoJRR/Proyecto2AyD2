package com.ayd.inventory_service.warehouse.controllers;

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
import com.ayd.inventory_service.warehouse.dtos.CreateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.SpecificationWarehouseDTO;
import com.ayd.inventory_service.warehouse.dtos.UpdateWarehouseRequestDTO;
import com.ayd.inventory_service.warehouse.dtos.WarehouseResponseDTO;
import com.ayd.inventory_service.warehouse.mappers.WarehouseMapper;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final ForWarehousePort forWarehousePort;
    private final WarehouseMapper warehouseMapper;

    @Operation(summary = "Obtener bodega por ID", description = "Devuelve la información de una bodega específica a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseResponseDTO getWarehouse(@PathVariable String id) throws NotFoundException {
        Warehouse warehouse = forWarehousePort.getWarehouse(id);
        return warehouseMapper.fromWarehouseToWarehouseResponseDTO(warehouse);
    }

    @Operation(summary = "Crear una nueva bodega", description = "Registra una nueva bodega en el sistema. Valida que no exista otra con los mismos datos únicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "409", description = "Ya existe una bodega con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseResponseDTO createWarehouse(
            @RequestBody @Valid CreateWarehouseRequestDTO createWarehouseRequestDTO)
            throws DuplicatedEntryException {
        Warehouse warehouse = forWarehousePort.createWarehouse(createWarehouseRequestDTO);
        return warehouseMapper.fromWarehouseToWarehouseResponseDTO(warehouse);
    }

    @Operation(summary = "Actualizar una bodega existente", description = "Actualiza completamente los datos de una bodega identificada por su ID. Valida existencia y duplicados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe otra bodega con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseResponseDTO updateWarehouse(@PathVariable String id,
            @RequestBody @Valid UpdateWarehouseRequestDTO updateWarehouseRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Warehouse warehouse = forWarehousePort.updateWarehouse(id, updateWarehouseRequestDTO);
        return warehouseMapper.fromWarehouseToWarehouseResponseDTO(warehouse);
    }

    @Operation(summary = "Alternar estado activo de una bodega", description = "Activa o desactiva una bodega según su estado actual. Requiere el ID de la bodega.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la bodega actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PatchMapping("/{id}/toogle")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseResponseDTO tootgleActive(@PathVariable String id) throws NotFoundException {
        Warehouse warehouse = forWarehousePort.tootgleActive(id);
        return warehouseMapper.fromWarehouseToWarehouseResponseDTO(warehouse);
    }

    @Operation(summary = "Obtener lista de bodegas", description = "Devuelve una lista de todas las bodegas registradas. Se pueden aplicar filtros opcionales mediante el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<WarehouseResponseDTO> getWarehouses(
            @RequestBody(required = false) SpecificationWarehouseDTO specificationWarehouseDTO) {
        List<Warehouse> warehouses = forWarehousePort.getWarehouses(specificationWarehouseDTO);
        return warehouseMapper.fromWarehouseListToWarehouseResponseDTOList(warehouses);
    }

}