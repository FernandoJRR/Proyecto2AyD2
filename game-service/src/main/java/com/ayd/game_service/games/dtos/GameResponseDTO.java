package com.ayd.game_service.games.dtos;

import java.util.List;

import com.ayd.game_service.players.dtos.PlayerResponseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor
@Data
public class GameResponseDTO {

    private String reservationId;
    private List<PlayerResponseDTO> players;
    private Boolean hasFinished;
}
