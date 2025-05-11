package com.ayd.invoice_service.Invoice.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.ayd.shared.exceptions.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponseDTO createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO createInvoiceRequestDTO)
            throws IllegalArgumentException, NotFoundException {
        Invoice invoice = forInvoicePort.createInvoice(createInvoiceRequestDTO);
        return invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponseDTO getInvoiceById(@PathVariable String id) throws NotFoundException {
        Invoice invoice = forInvoicePort.getInvoiceById(id);
        return invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice);
    }

    @GetMapping("/client/{clientDocument}")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceResponseDTO> getInvoicesByClientDocument(@PathVariable String clientDocument)
            throws NotFoundException {
        List<Invoice> invoices = forInvoicePort.getInvoicesByClientDocument(clientDocument);
        return invoiceMapper.fromInvoicesToInvoiceResponseDTOs(invoices);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceResponseDTO> getAllInvoices(
            @RequestBody(required = false) SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO)
            throws NotFoundException {
        List<Invoice> invoices = forInvoicePort.getAllInvoices(specificationInvoiceRequestDTO);
        return invoiceMapper.fromInvoicesToInvoiceResponseDTOs(invoices);
    }
}
