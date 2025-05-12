package com.ayd.game_service.games.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ScoreGameRequestDTO {
    @NotNull(message = "El numero de hoyo es obligatorio")
    private Integer holeNumber;

    @Valid
    private List<ScorePlayerRequestDTO> scorePlayers;
}
