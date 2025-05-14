package com.ayd.game_service.games.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;
import com.ayd.game_service.games.dtos.ScoreGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameResponseDTO;
import com.ayd.game_service.games.mappers.GamesMapper;
import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.games.ports.ForGamesPort;
import com.ayd.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private final ForGamesPort forGamesPort;
    private final GamesMapper gamesMapper;

    @Operation(summary = "Obtener todos los juegos", description = "Este endpoint permite la obtencion de todos los juegos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Punteo obtenido exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GameResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> getGames()
            throws NotFoundException, IllegalArgumentException {
        List<Game> result = forGamesPort.getGames();
        List<GameResponseDTO> response = gamesMapper.fromGamesToResponseDTO(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Crear un nuevo juego", description = "Este endpoint permite la creación de un nuevo juego en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Juego creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_GAME')")
    public ResponseEntity<GameResponseDTO> createGame(
            @RequestBody @Valid CreateGameRequestDTO request)
            throws NotFoundException {
        Game result = forGamesPort.createGame(request);

        GameResponseDTO response = gamesMapper.fromGameToResponseDTO(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear un nuevo juego sin permisos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Juego creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/free")
    public ResponseEntity<GameResponseDTO> createGameFree(
            @RequestBody @Valid CreateGameRequestDTO request)
            throws NotFoundException {
        Game result = forGamesPort.createGame(request);

        GameResponseDTO response = gamesMapper.fromGameToResponseDTO(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener un juego por su id", description = "Este endpoint permite la obtencion un juego en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Juego obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Juego no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDTO> getGameById(
            @PathVariable("gameId") String gameId)
            throws NotFoundException {
        Game result = forGamesPort.getGameById(gameId);

        GameResponseDTO response = gamesMapper.fromGameToResponseDTO(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener un juego por el id de la reservacion", description = "Este endpoint permite la obtencion un juego por reservacion en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Juego obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Juego no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<GameResponseDTO> getGameByReservationId(
            @PathVariable("reservationId") String reservationId)
            throws NotFoundException {
        Game result = forGamesPort.getGameByReservationId(reservationId);

        GameResponseDTO response = gamesMapper.fromGameToResponseDTO(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Actualizar el punteo de un juego", description = "Este endpoint permite la actualizacion del punteo de un juego del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Punteo actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Juego no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/score/{gameId}")
    @PreAuthorize("hasAuthority('CREATE_GAME')")
    public ResponseEntity<GameResponseDTO> updateScore(
            @PathVariable("gameId") String gameId,
            @RequestBody @Valid ScoreGameRequestDTO request)
            throws NotFoundException, IllegalArgumentException {
        Game result = forGamesPort.updateScore(gameId, request);

        GameResponseDTO response = gamesMapper.fromGameToResponseDTO(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener el punteo actual de un juego", description = "Este endpoint permite la obtencion del punteo actual de un juego.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Punteo obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Juego no encontrado.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/score/{gameId}")
    public ResponseEntity<ScoreGameResponseDTO> getScore(
            @PathVariable("gameId") String gameId)
            throws NotFoundException, IllegalArgumentException {
        ScoreGameResponseDTO result = forGamesPort.getScore(gameId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
