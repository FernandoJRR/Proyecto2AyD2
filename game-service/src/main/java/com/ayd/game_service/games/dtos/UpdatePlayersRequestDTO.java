package com.ayd.game_service.games.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePlayersRequestDTO {
    List<PlayerNameRequestDTO> players;
}
