package com.ayd.game_service.games.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScoreGameRequestDTO {
    @NotNull(message = "El numero de hoyo es obligatorio")
    private Integer holeNumber;

    @Valid
    private List<ScorePlayerRequestDTO> scorePlayers;
}
