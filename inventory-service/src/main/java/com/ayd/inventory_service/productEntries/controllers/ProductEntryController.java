package com.ayd.inventory_service.productEntries.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryRequestDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntryResponseDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntrySpecificationDTO;
import com.ayd.inventory_service.productEntries.mappers.ProductEntryMapper;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.ports.ForProductEntryPort;
import com.ayd.shared.exceptions.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product-entries")
@RequiredArgsConstructor
public class ProductEntryController {
    private final ForProductEntryPort forProductEntryPort;
    private final ProductEntryMapper productEntryMapper;

    @Operation(summary = "Obtener entrada de producto por ID", description = "Devuelve la información de una entrada de producto específica a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrada de producto encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrada de producto no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductEntryResponseDTO getProductEntry(@PathVariable String id) throws NotFoundException {
        ProductEntry productEntry = forProductEntryPort.getProductEntryById(id);
        return productEntryMapper.formProductEntryToProductEntryResponseDTO(productEntry);
    }

    @Operation(summary = "Obtener entrada de producto por número de factura", description = "Devuelve la información de una entrada de producto utilizando su número de factura como identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrada de producto encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entrada de producto no encontrada para el número de factura especificado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/invoice-number/{invoiceNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ProductEntryResponseDTO getProductEntryByInvoiceNumber(@PathVariable String invoiceNumber)
            throws NotFoundException {
        ProductEntry productEntry = forProductEntryPort.getProductEntryByInvoiceNumber(invoiceNumber);
        return productEntryMapper.formProductEntryToProductEntryResponseDTO(productEntry);
    }

    @Operation(summary = "Obtener todas las entradas de producto", description = "Devuelve una lista de entradas de producto registradas en el sistema. Se pueden aplicar filtros opcionales mediante el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de entradas de producto obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los filtros"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductEntryResponseDTO> getAllProductEntries(
            @RequestBody(required = false) ProductEntrySpecificationDTO productEntrySpecificationDTO) {
        List<ProductEntry> productEntries = forProductEntryPort
                .getAlByProductEntrieSpecification(productEntrySpecificationDTO);
        return productEntryMapper.formProductEntryListToProductEntryResponseDTOList(productEntries);
    }

    @Operation(summary = "Registrar una nueva entrada de producto", description = "Crea una nueva entrada de producto en el sistema. Valida duplicados, existencia de entidades relacionadas y posibles conflictos de estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrada de producto registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Entidad relacionada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto: ya existe una entrada o hay un estado inválido"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('CREATE_PRODUCT_ENTRY')")
    public ProductEntryResponseDTO createProductEntry(@Valid @RequestBody ProductEntryRequestDTO productEntryRequestDTO)
            throws NotFoundException, DuplicatedEntryException, IllegalStateException {
        ProductEntry productEntry = forProductEntryPort.saveProductEntry(productEntryRequestDTO);
        return productEntryMapper.formProductEntryToProductEntryResponseDTO(productEntry);
    }
}
