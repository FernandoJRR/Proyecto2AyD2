package com.ayd.game_service.games.dtos;

import java.util.List;

import com.ayd.game_service.players.dtos.PlayerResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GameResponseDTO {

    private String reservationId;
    private List<PlayerResponseDTO> players;
    private boolean hasFinished;
}
