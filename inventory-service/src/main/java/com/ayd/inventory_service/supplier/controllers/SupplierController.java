package com.ayd.inventory_service.supplier.controllers;

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

import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.supplier.dtos.CreateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.SpecificationSupplierRequestDTO;
import com.ayd.inventory_service.supplier.dtos.SupplierResponseDTO;
import com.ayd.inventory_service.supplier.dtos.UpdateSupplierRequestDTO;
import com.ayd.inventory_service.supplier.mappers.SupplierMapper;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.ports.ForSupplierPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final ForSupplierPort forSupplierPort;
    private final SupplierMapper supplierMapper;

    @Operation(summary = "Obtener proveedor por ID", description = "Devuelve la información de un proveedor específico a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponseDTO getSupplierById(@PathVariable String id) throws NotFoundException {
        Supplier supplier = forSupplierPort.getSupplierById(id);
        return supplierMapper.fromSupplierToSupplierResponseDTO(supplier);
    }

    @Operation(summary = "Registrar un nuevo proveedor", description = "Crea un nuevo proveedor en el sistema. Valida que no exista otro proveedor con el mismo NIT u otros datos únicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "409", description = "Ya existe un proveedor con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponseDTO saveSupplier(@RequestBody @Valid CreateSupplierRequestDTO supplierRequestDTO)
            throws DuplicatedEntryException {
        Supplier supplier = forSupplierPort.saveSupplier(supplierRequestDTO);
        return supplierMapper.fromSupplierToSupplierResponseDTO(supplier);
    }

    @Operation(summary = "Actualizar proveedor existente", description = "Actualiza los datos de un proveedor identificado por su ID. Valida que exista y que no haya conflicto con datos ya existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto con los datos actualizados"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponseDTO updateSupplier(@RequestBody @Valid UpdateSupplierRequestDTO supplierRequestDTO,
            @PathVariable String id) throws NotFoundException, IllegalStateException {
        Supplier supplier = forSupplierPort.updateSupplier(supplierRequestDTO, id);
        return supplierMapper.fromSupplierToSupplierResponseDTO(supplier);
    }

    @Operation(summary = "Alternar estado de proveedor", description = "Activa o desactiva un proveedor según su estado actual. Requiere el identificador del proveedor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del proveedor actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PatchMapping("/{id}/toggle")
    @ResponseStatus(HttpStatus.OK)
    public SupplierResponseDTO toggleSupplierStatus(@PathVariable String id) throws NotFoundException {
        Supplier supplier = forSupplierPort.toogleSupplierStatus(id);
        return supplierMapper.fromSupplierToSupplierResponseDTO(supplier);
    }

    @Operation(summary = "Obtener lista de proveedores", description = "Devuelve todos los proveedores registrados. Se pueden aplicar filtros opcionales mediante un cuerpo de solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<SupplierResponseDTO> getAllSuppliers(
            @RequestBody(required = false) SpecificationSupplierRequestDTO specificationSupplierRequestDTO) {
        List<Supplier> suppliers = forSupplierPort.getSuppliersBySpecification(specificationSupplierRequestDTO);
        return supplierMapper.fromSupplierListToSupplierResponseDTOList(suppliers);
    }
}
