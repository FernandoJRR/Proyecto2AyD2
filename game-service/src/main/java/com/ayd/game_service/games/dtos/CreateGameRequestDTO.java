package com.ayd.game_service.games.dtos;

import java.util.List;

import com.ayd.game_service.players.dtos.CreatePlayerRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateGameRequestDTO {
    @NotBlank(message = "El id del paquete es obligatorio")
    private String packageId;

    @NotBlank(message = "El id de la reservacion es obligatorio")
    private String reservationId;

    @Valid
    private List<CreatePlayerRequestDTO> players;
}
