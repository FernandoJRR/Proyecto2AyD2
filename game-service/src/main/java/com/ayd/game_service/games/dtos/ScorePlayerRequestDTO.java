package com.ayd.game_service.games.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScorePlayerRequestDTO {
    @NotBlank(message = "El id del jugador es obligatorio")
    private String playerId;

    @NotNull(message = "El numero de tiros del jugador es obligatorio")
    private Integer shotsPlayer;
}
