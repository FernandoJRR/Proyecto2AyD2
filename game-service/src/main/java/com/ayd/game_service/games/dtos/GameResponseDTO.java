package com.ayd.game_service.games.dtos;

import java.util.List;

import com.ayd.game_service.players.dtos.PlayerResponseDTO;

import lombok.Value;

@Value
public class GameResponseDTO {

    private String reservationId;
    private List<PlayerResponseDTO> players;
    private Boolean hasFinished;
}
