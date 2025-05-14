package com.ayd.game_service.games.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayerNameRequestDTO {
    private String name;
    private Integer playerNumber;
}
