package com.ayd.employee_service.employees.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.employees.dtos.HistoryTypeResponseDTO;
import com.ayd.employee_service.employees.mappers.HistoryTypeMapper;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForHistoryTypePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/history-types")

@RequiredArgsConstructor
public class HistoryTypeController {

        private final ForHistoryTypePort historyTypePort;
        private final HistoryTypeMapper historyTypeMapper;

        @Operation(summary = "Obtener los tipos de historial para desactivacion", description = "Devuelve la lista de los tipos de historial para realizar desactivaciones.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de tipos de empleados obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/deactivation")
        @ResponseStatus(HttpStatus.OK)
        public List<HistoryTypeResponseDTO> getDeactivationHistoryTypes() {

                // se obtienen los tipos de historial que se buscan
                List<HistoryType> result = historyTypePort.findDeactivationHistoryTypes();

                // convertir el Employee al dto
                List<HistoryTypeResponseDTO> response = historyTypeMapper
                                .fromHistoryTypesToHistoryTypeResponseDTOs(result);

                return response;
        }

        @Operation(summary = "Obtener todos los tipos de historial", description = "Devuelve una lista de todos los tipos de historial de empleados disponibles en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/all")
        @ResponseStatus(HttpStatus.OK)
        public List<HistoryTypeResponseDTO> getAllHistoryTypes() {

                // se obtienen los todos tipos de historial
                List<HistoryType> result = historyTypePort.findAll();

                // convertir el Employee al dto
                List<HistoryTypeResponseDTO> response = historyTypeMapper
                                .fromHistoryTypesToHistoryTypeResponseDTOs(result);

                return response;
        }
}
