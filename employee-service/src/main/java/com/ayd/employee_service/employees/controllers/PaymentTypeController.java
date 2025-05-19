package com.ayd.employee_service.employees.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.employees.dtos.HistoryTypeResponseDTO;
import com.ayd.employee_service.employees.dtos.PaymentTypeResponseDTO;
import com.ayd.employee_service.employees.mappers.HistoryTypeMapper;
import com.ayd.employee_service.employees.mappers.PaymentTypeMapper;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.models.PaymentType;
import com.ayd.employee_service.employees.ports.ForHistoryTypePort;
import com.ayd.employee_service.employees.ports.ForPaymentTypePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment-types")

@RequiredArgsConstructor
public class PaymentTypeController {

        private final ForPaymentTypePort forPaymentTypePort;
        private final PaymentTypeMapper paymentTypeMapper;

        @Operation(summary = "Obtener todos los tipos de extra", description = "Devuelve una lista de todos los tipos de extra disponibles en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<PaymentTypeResponseDTO> getAllHistoryTypes() {

                List<PaymentType> result = forPaymentTypePort.findAll();

                List<PaymentTypeResponseDTO> response = paymentTypeMapper
                                .fromPaymentTypesToResponseDTO(result);

                return response;
        }
}
