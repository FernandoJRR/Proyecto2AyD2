package com.ayd.game_service.players.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreatePlayerRequestDTO {
    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String name;
    @NotNull(message = "El numero de jugador es obligatorio")
    private Integer playerNumber;
}
