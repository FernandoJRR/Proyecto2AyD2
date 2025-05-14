package com.ayd.reservation_service.reservation.ports;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;

public interface ForGameClientPort {

    public GameResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO);
}
