package com.ayd.game_service.games.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ScorePlayerResponseDTO {
    private String id;
    private String name;
    private Integer totalShots;
}
