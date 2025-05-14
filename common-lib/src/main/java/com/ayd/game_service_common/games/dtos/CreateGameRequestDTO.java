package com.ayd.game_service_common.games.dtos;

import java.util.List;

import com.ayd.game_service_common.players.dtos.CreatePlayerRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateGameRequestDTO {
    @NotBlank(message = "El id de la reservacion es obligatorio")
    private String reservationId;

    @Valid
    private List<CreatePlayerRequestDTO> players;
}
