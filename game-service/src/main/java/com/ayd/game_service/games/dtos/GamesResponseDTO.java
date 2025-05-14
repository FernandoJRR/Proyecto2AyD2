package com.ayd.game_service.games.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GamesResponseDTO {
    List<GameResponseDTO> games;
}
