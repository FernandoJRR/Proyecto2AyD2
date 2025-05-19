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

import com.ayd.employee_service.employees.dtos.CreateExtraPaymentDTO;
import com.ayd.employee_service.employees.dtos.ExtraPaymentResponseDTO;
import com.ayd.employee_service.employees.mappers.ExtraPaymentMapper;
import com.ayd.employee_service.employees.models.ExtraPayment;
import com.ayd.employee_service.employees.ports.ForExtraPaymentsPort;
import com.ayd.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/extra-payments")

@RequiredArgsConstructor
public class ExtraPaymentsController {
        private final ForExtraPaymentsPort forExtraPaymentsPort;
        private final ExtraPaymentMapper extraPaymentMapper;

        @Operation(summary = "Crear un pago o descuento extra", description = "Crea un pago o descuento extra.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago extra creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping
        @ResponseStatus(HttpStatus.OK)
        public ExtraPaymentResponseDTO createExtraPayment(
            @RequestBody @Valid CreateExtraPaymentDTO request) throws NotFoundException{

                ExtraPayment result = forExtraPaymentsPort.createExtraPayment(request);

                ExtraPaymentResponseDTO response = extraPaymentMapper
                    .fromExtraPaymentToResponseDTO(result);

                return response;
        }

        @Operation(summary = "Obtiene todos los pagos y descuentos extra", description = "Endpoint para obtener todos los pagos y descuentos extras del sistema.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<ExtraPaymentResponseDTO> getAllPayments() throws NotFoundException{

                List<ExtraPayment> result = forExtraPaymentsPort.getAllExtraPayments();

                List<ExtraPaymentResponseDTO> response = extraPaymentMapper
                    .fromExtraPaymentsToResponseDTO(result);

                return response;
        }

        @Operation(summary = "Obtiene un extra por su id", description = "Endpoint para obtener un pago por su id en el sistema")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente."),
            @ApiResponse(responseCode = "404", description = "Extra no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{paymentId}")
        @ResponseStatus(HttpStatus.OK)
        public ExtraPaymentResponseDTO getPaymentById(
            @PathVariable("paymentId") String paymentId) throws NotFoundException {

                ExtraPayment result = forExtraPaymentsPort.getPaymentById(paymentId);

                ExtraPaymentResponseDTO response = extraPaymentMapper
                    .fromExtraPaymentToResponseDTO(result);

                return response;
        }
}
