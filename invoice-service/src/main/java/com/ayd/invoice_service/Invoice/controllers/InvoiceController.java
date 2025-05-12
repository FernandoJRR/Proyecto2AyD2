package com.ayd.invoice_service.Invoice.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.InvoiceResponseDTO;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.mappers.InvoiceMapper;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.ports.ForInvoicePort;
import com.ayd.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final ForInvoicePort forInvoicePort;
    private final InvoiceMapper invoiceMapper;

    @Operation(summary = "Crear una nueva factura", description = "Registra una nueva factura en el sistema. Valida la existencia de entidades relacionadas y la estructura de los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Entidad relacionada no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CREATE_INVOICE')")
    public InvoiceResponseDTO createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO createInvoiceRequestDTO)
            throws IllegalArgumentException, NotFoundException {
        Invoice invoice = forInvoicePort.createInvoice(createInvoiceRequestDTO);
        return invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice);
    }

    @Operation(summary = "Obtener factura por ID", description = "Devuelve la información de una factura específica a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponseDTO getInvoiceById(@PathVariable String id) throws NotFoundException {
        Invoice invoice = forInvoicePort.getInvoiceById(id);
        return invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice);
    }

    @Operation(summary = "Obtener facturas por documento de cliente", description = "Devuelve una lista de facturas asociadas al documento de identificación de un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente o facturas no encontradas"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/client/{clientDocument}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceResponseDTO> getInvoicesByClientDocument(@PathVariable String clientDocument)
            throws NotFoundException {
        List<Invoice> invoices = forInvoicePort.getInvoicesByClientDocument(clientDocument);
        return invoiceMapper.fromInvoicesToInvoiceResponseDTOs(invoices);
    }

    @Operation(summary = "Obtener lista de facturas", description = "Devuelve todas las facturas registradas en el sistema. Se pueden aplicar filtros opcionales mediante el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron facturas con los filtros aplicados"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceResponseDTO> getAllInvoices(
            @RequestBody(required = false) SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO)
            throws NotFoundException {
        List<Invoice> invoices = forInvoicePort.getAllInvoices(specificationInvoiceRequestDTO);
        return invoiceMapper.fromInvoicesToInvoiceResponseDTOs(invoices);
    }
}
