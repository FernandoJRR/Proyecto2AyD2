package com.ayd.reservation_service.reservation.ports;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;

public interface ForGameClientPort {

    public Object createGame(CreateGameRequestDTO createGameRequestDTO);
}
