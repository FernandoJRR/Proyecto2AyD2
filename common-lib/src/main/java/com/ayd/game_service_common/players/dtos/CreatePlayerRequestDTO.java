package com.ayd.game_service_common.players.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePlayerRequestDTO {
    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String name;
    @NotNull(message = "El numero de jugador es obligatorio")
    private Integer playerNumber;
}
