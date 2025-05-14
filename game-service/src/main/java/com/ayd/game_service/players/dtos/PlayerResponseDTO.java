package com.ayd.game_service.players.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
public class PlayerResponseDTO {
    private String id;
    private String name;
    private Integer playerNumber;
}
