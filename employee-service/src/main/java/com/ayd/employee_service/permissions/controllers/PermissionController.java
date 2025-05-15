package com.ayd.employee_service.permissions.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.employee_service.permissions.mappers.PermissionMapper;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.ports.ForPermissionsPort;
import com.ayd.sharedEmployeeService.dto.PermissionResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final ForPermissionsPort forPermissionsPort;
    private final PermissionMapper permissionMapper;

    @Operation(summary = "Obtener todos los permisos", description = "Este endpoint permite la obtención de todos los permisos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permisos obtenidos exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CREATE_EMPLOYEE_TYPE', 'EDIT_EMPLOYEE_TYPE')")
    public List<PermissionResponseDTO> findPermissions() {
        // mandamos a traer todos los permisos
        List<Permission> result = forPermissionsPort.findAllPemrissions();
        // convertir el la lista a lista de dtos
        List<PermissionResponseDTO> response = permissionMapper.fromPermissionsToPermissionsReponseDtos(result);
        return response;
    }
}
