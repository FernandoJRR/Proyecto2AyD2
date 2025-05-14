package com.ayd.game_service.games.dtos;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScoreGameResponseDTO {
    private List<ScorePlayerResponseDTO> playerScores;
}
