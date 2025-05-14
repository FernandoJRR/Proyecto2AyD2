package com.ayd.product_service.packages.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.ayd.product_service.packages.dtos.SaveGolfPackageRequestDTO;
import com.ayd.product_service.packages.mappers.GolfPackageMapper;
import com.ayd.product_service.packages.models.GolfPackage;
import com.ayd.product_service.packages.ports.ForGolfPackagePort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class GolfPackageController {
    private final GolfPackageMapper golfPackageMapper;
    private final ForGolfPackagePort forGolfPackagePort;

    @Operation(summary = "Crea un paquete de juego")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paquete creado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro un produto"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CREATE_GOLF_PACKAGE')")
    public GolfPackageResponseDTO createPackage(@Valid @RequestBody SaveGolfPackageRequestDTO dto)
            throws NotFoundException {
        // convertir el dto entidad
        GolfPackage golfPackage = golfPackageMapper.fromCreateDtoToPackage(dto);
        // mandamos a crear el paquete
        GolfPackage createdPackage = forGolfPackagePort.createGolfPackage(golfPackage);
        // convertimos lo creado a dto
        return golfPackageMapper.fromGolfPackageToGolfPackageResponseDTO(createdPackage);
    }

    @Operation(summary = "Edita un paquete de juego")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paquete editado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro un producto"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_GOLF_PACKAGE')")
    @ResponseStatus(HttpStatus.OK)
    public GolfPackageResponseDTO editPackage(@Valid @RequestBody SaveGolfPackageRequestDTO dto,
            @PathVariable String id) throws NotFoundException {
        // convertir el dto entidad
        GolfPackage golfPackage = golfPackageMapper.fromCreateDtoToPackage(dto);
        // mandamos a crear el paquete
        GolfPackage editedPackage = forGolfPackagePort.updateGolfPackage(id, golfPackage);
        // convertimos lo creado a dto
        return golfPackageMapper.fromGolfPackageToGolfPackageResponseDTO(editedPackage);
    }

    @Operation(summary = "Trae un paquete de juego por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paquete encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro un producto"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GolfPackageResponseDTO getGolfPackageById(
            @PathVariable String id) throws NotFoundException {
        // mandamos a crear el paquete
        GolfPackage editedPackage = forGolfPackagePort.getGolfPackageById(id);
        // convertimos lo creado a dto
        return golfPackageMapper.fromGolfPackageToGolfPackageResponseDTO(editedPackage);
    }

    @Operation(summary = "Trae un paquete de juego por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paquete encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro un producto"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GolfPackageResponseDTO> getAllGolfPackages() {
        // mandamos a crear el paquete
        List<GolfPackage> packages = forGolfPackagePort.getAllGolfPackages();
        // convertimos lo creado a dto
        return golfPackageMapper.fromList_GolfPackageToList_GolfPackageResponseDTO(packages);
    }

}
