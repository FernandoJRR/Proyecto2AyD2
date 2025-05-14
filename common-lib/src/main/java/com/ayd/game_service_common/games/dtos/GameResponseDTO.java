package com.ayd.game_service_common.games.dtos;

import java.util.List;

import com.ayd.game_service_common.players.dtos.PlayerResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameResponseDTO {

    private String id;
    private String reservationId;
    private List<PlayerResponseDTO> players;
    private Boolean hasFinished;
}
