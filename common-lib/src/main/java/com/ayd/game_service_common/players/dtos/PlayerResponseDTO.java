package com.ayd.game_service_common.players.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerResponseDTO {
    private String name;
    private Integer playerNumber;
}
