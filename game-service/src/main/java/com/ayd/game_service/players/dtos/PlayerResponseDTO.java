package com.ayd.game_service.players.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class PlayerResponseDTO {
    private String name;
    private Integer playerNumber;
}
