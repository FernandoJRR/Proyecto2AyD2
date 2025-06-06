package com.ayd.config_service.parameters.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.config_service.parameters.dtos.ParameterDiasRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterNITRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterNombreRequestDTO;
import com.ayd.config_service.parameters.dtos.ParameterRegimenRequestDTO;
import com.ayd.config_service.parameters.enums.ParameterEnum;
import com.ayd.config_service.parameters.mappers.ParameterMapper;
import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.parameters.ports.ForParameterPort;
import com.ayd.shared.exceptions.*;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/config")

@RequiredArgsConstructor
public class ParametersControllers {
    private final ForParameterPort parameterPort;
    private final ParameterMapper parameterMapper;

        @Operation(summary = "Permite obtener el NIT de la empresa", description = "Este endpoint permite la obtencion del NIT de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "NIT obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Peticion invalida, revisa los parametros enviados", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/nit")
        public ResponseEntity<ParameterResponseDTO> getNITEmpresa() throws NotFoundException {
                Parameter result = parameterPort.findParameterByKey(ParameterEnum.NIT_EMPRESA.getKey());

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite obtener el NIT de la empresa", description = "Este endpoint permite la obtencion del NIT de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "NIT obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/nit")
        @PreAuthorize("hasAuthority('UPDATE_CONFIG')")
        public ResponseEntity<ParameterResponseDTO> updateNITEmpresa(
            @RequestBody @Valid ParameterNITRequestDTO request
        )
            throws NotFoundException, InvalidParameterException {
                String newNIT = request.getNit();

                Parameter result = parameterPort.updateNITEmpresa(newNIT);

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite obtener el nombre de la empresa", description = "Este endpoint permite la obtencion del nombre de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Nombre obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/nombre")
        public ResponseEntity<ParameterResponseDTO> getNombreEmpresa() throws NotFoundException {
                Parameter result = parameterPort.findParameterByKey(ParameterEnum.NOMBRE_EMPRESA.getKey());

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite actualizar el nombre de la empresa", description = "Este endpoint permite la actualizacion del nombre de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Nombre obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/nombre")
        @PreAuthorize("hasAuthority('UPDATE_CONFIG')")
        public ResponseEntity<ParameterResponseDTO> updateNombreEmpresa(
            @RequestBody @Valid ParameterNombreRequestDTO request
        )
            throws NotFoundException, InvalidParameterException {
                String newName = request.getNewName();

                Parameter result = parameterPort.updateNombreEmpresa(newName);

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite obtener el regimen de la empresa", description = "Este endpoint permite la obtencion del regimen de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Regimen obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/regimen")
        public ResponseEntity<ParameterResponseDTO> getRegimenEmpresa() throws NotFoundException {
                Parameter result = parameterPort.findParameterByKey(ParameterEnum.REGIMEN_EMPRESA.getKey());

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite actualizar el regimen de la empresa", description = "Este endpoint permite la actualizacion del regimen de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Regimen actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Peticion invalida, revisa los parametros enviados", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/regimen")
        @PreAuthorize("hasAuthority('UPDATE_CONFIG')")
        public ResponseEntity<ParameterResponseDTO> updateRegimenEmpresa(
            @RequestBody @Valid ParameterRegimenRequestDTO request
        ) throws NotFoundException, InvalidParameterException {
                String newRegimen = request.getNewRegime();

                Parameter result = parameterPort.updateRegimenEmpresa(newRegimen);

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite obtener los dias de vacaciones de la empresa", description = "Este endpoint permite la obtencion los dias de vacaciones de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Regimen obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/dias-vacaciones")
        public ResponseEntity<ParameterResponseDTO> getDiasVacaciones() throws NotFoundException {
                Parameter result = parameterPort.findParameterByKey(ParameterEnum.DIAS_VACACIONES.getKey());

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "Permite actualizar los dias de vacaciones de la empresa", description = "Este endpoint permite la actualizacion de los dias de vacaciones de la empresa.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Dias actualizados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParameterResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Peticion invalida, revisa los parametros enviados", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Parametro no encontrado", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PatchMapping("/dias-vacaciones")
        @PreAuthorize("hasAuthority('UPDATE_CONFIG')")
        public ResponseEntity<ParameterResponseDTO> updateDiasVacaciones(
            @RequestBody @Valid ParameterDiasRequestDTO request
        ) throws NotFoundException, InvalidParameterException {
                String newDays = request.getNewDays();

                Parameter result = parameterPort.updateRegimenEmpresa(newDays);

                ParameterResponseDTO response = parameterMapper.fromParameterToResponse(result);

                return ResponseEntity.status(HttpStatus.OK).body(response);
        }
}
